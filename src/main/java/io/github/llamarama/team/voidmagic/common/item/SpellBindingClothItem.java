package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.common.util.IdHelper;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
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

/**
 * Fancy shiny item, and stuff.
 *
 * @author 0xJoeMama
 * @since 2021
 */
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
        // Make sure we are on the server for logic.
        if (!world.isRemote()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity == null)
                return ActionResultType.PASS;

            // Generate the itemstack of packed blocks that would be good for the target.
            ItemStack resultStack = this.getItemStackFromTarget(tileEntity, world, pos);
            // Make sure no items are left before we break.
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

    /**
     * Used to make an {@link ItemStack} with the correct NBT tag for an item usage.
     *
     * @param tileEntity The target inventory.
     * @param world      The world that entity exists in.
     * @param pos        The position of that entity.
     * @return An {@link ItemStack} of {@link PackedBlockItem} that contains the correct tag.
     */
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

        // Actually put the correct number in.
        stackOut.getOrCreateTag().putInt(NBTConstants.FILLED_STACKS, filledStacks);

        // Return the stack.
        return stackOut;
    }

    /**
     * Create the tag that contains the block ID and the contents of the inventory.
     *
     * @param containerAccess The getItemStackInSlot type method of the target inventory.
     * @param size            The size of the container.
     * @param stackOut        That stack whose tag needs to be filled.
     */
    protected void fillTag(Function<Integer, ItemStack> containerAccess, int size, ItemStack stackOut) {
        ListNBT inventoryTag = this.makeInventoryTag(containerAccess, size);
        this.addInventoryTag(stackOut, inventoryTag);
    }

    /**
     * Adds the tag that contains the contents of the inventory.
     *
     * @param stackOut         The stack whose tag will be modified.
     * @param containerItemTag The content tag.
     */
    public void addInventoryTag(ItemStack stackOut, ListNBT containerItemTag) {
        CompoundNBT stackTag = stackOut.getOrCreateTag();
        stackTag.put(NBTConstants.INVENTORY, containerItemTag);
    }

    /**
     * Creates the tag with the contents of the inventory.
     *
     * @param provider The getStackInSlot type method.
     * @param size     The size of the inventory.
     * @return The tag with the contents of the inventory.
     */
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
