package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.util.IdHelper;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SpellBindingClothItem extends Item {

    public static final String SHINY_KEY = "item.voidmagic.spellbinding_cloth.shiny";

    public SpellBindingClothItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();

        if (playerEntity == null || !playerEntity.isSneaking())
            return ActionResultType.PASS;
        if (!world.isRemote()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity == null)
                return ActionResultType.PASS;


            ItemStack resultStack = this.getItemStackFromTarget(tileEntity, world, pos);
            this.breakWithNoItemDrops(pos, world);
            ItemEntity toSpawn =
                    new ItemEntity(world, pos.getX() + 0.5d, pos.getY() + 0.5f, pos.getZ() + 0.5f, resultStack);

            if (world.addEntity(toSpawn)) {
                ItemStack activeStack = playerEntity.getHeldItem(context.getHand());
                Item heldItem = activeStack.getItem();
                if (heldItem instanceof SpellBindingClothItem)
                    activeStack.shrink(1);
            }
        }
        return ActionResultType.SUCCESS;
    }

    protected void breakWithNoItemDrops(BlockPos pos, World world) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity == null)
            return;

        if (tileEntity instanceof IInventory) {
            Consumer<Integer> removeFunction = ((IInventory) tileEntity)::removeStackFromSlot;

            for (int i = 0; i < ((IInventory) tileEntity).getSizeInventory(); i++) {
                removeFunction.accept(i);
            }
        }

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler) -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                itemHandler.extractItem(0, itemHandler.getStackInSlot(i).getCount(), false);
            }
        });

        world.removeBlock(pos, false);
    }

    public ItemStack getItemStackFromTarget(TileEntity tileEntity, World world, BlockPos pos) {
        ItemStack stackOut = new ItemStack(ModItems.PACKED_BLOCK.get(), 1);
        // The block that will later be written to the stack nbt.
        BlockState state = world.getBlockState(pos);

        /*
            First we get the items from the inventory/
         */
        // Check if its a vanilla inventory.
        if (tileEntity instanceof IInventory)
            this.fillTag(((IInventory) tileEntity)::getStackInSlot,
                    ((IInventory) tileEntity).getSizeInventory(), stackOut);

        // Use capabilities to get the items if it's a modded TE.
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler ->
                this.fillTag(itemHandler::getStackInSlot, itemHandler.getSlots(), stackOut));

        /*
            Then we write the the id of the block that we recorded earlier.
         */
        stackOut.getOrCreateTag().putString(NBTConstants.BLOCK_ID, IdHelper.getIdString(state.getBlock()));


        // Put the amount of filled stacks just for reading for the tooltip.
        ListNBT inventory = (ListNBT) stackOut.getOrCreateTag().get(NBTConstants.INVENTORY);
        if (inventory == null)
            return stackOut;

        int filledStacks = 0;
        int i = 0;
        while (i < inventory.size()) {
            CompoundNBT stackTag = (CompoundNBT) inventory.get(i);
            if (!ItemStack.read(stackTag).isEmpty())
                ++filledStacks;

            i++;
        }

        stackOut.getOrCreateTag().putInt(NBTConstants.FILLED_STACKS, filledStacks);

        return stackOut;
    }

    protected void fillTag(Function<Integer, ItemStack> containerAccess, int size, ItemStack stackOut) {
        ListNBT inventoryTag = this.makeInventoryTag(containerAccess, size);
        this.addInventoryTag(stackOut, inventoryTag);
    }

    public void addInventoryTag(ItemStack stackOut, ListNBT containerItemTag) {
        CompoundNBT stackTag = stackOut.getOrCreateTag();
        stackTag.put(NBTConstants.INVENTORY, containerItemTag);
    }

    protected ListNBT makeInventoryTag(Function<Integer, ItemStack> provider, int size) {
        ListNBT containerItemTag = new ListNBT();

        for (int i = 0; i < size; i++) {
            ItemStack stackInSlot = provider.apply(i);
            CompoundNBT currentStackToNBT = stackInSlot.write(new CompoundNBT());
            containerItemTag.add(i, currentStackToNBT);
        }

        return containerItemTag;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        TranslationTextComponent textComponent = new TranslationTextComponent(SHINY_KEY);
        textComponent.modifyStyle((style) -> style.setItalic(true).setFormatting(TextFormatting.AQUA));
        tooltip.add(textComponent);
    }

}
