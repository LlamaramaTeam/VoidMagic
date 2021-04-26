package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.util.IdHelper;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Consumer;
import java.util.function.Function;

public class SpellBindingClothItem extends Item {

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

        if (tileEntity instanceof IInventory) {
            Consumer<Integer> function = ((IInventory) tileEntity)::removeStackFromSlot;

            for (int i = 0; i < ((IInventory) tileEntity).getSizeInventory(); i++) {
                function.accept(i);
            }
        }

        world.removeBlock(pos, false);
    }

    public ItemStack getItemStackFromTarget(TileEntity tileEntity, World world, BlockPos pos) {
        ItemStack stackOut = new ItemStack(ModItems.PACKED_BLOCK.get(), 1);
        BlockState state = world.getBlockState(pos);

        // Check if its a vanilla inventory.
        if (tileEntity instanceof IInventory)
            this.makeFromVanillaInterface((IInventory) tileEntity, stackOut);

        // Use capabilities to get the items if it's a modded TE.
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .ifPresent(itemHandler -> this.makeFromItemCapability(itemHandler, stackOut));

        // Put the ID.
        stackOut.getOrCreateTag().putString(NBTConstants.BLOCK_ID, IdHelper.getIdString(state.getBlock()));

        return stackOut;
    }

    protected void makeFromItemCapability(IItemHandler itemHandler, ItemStack stackOut) {
        ListNBT inventoryTag = this.makeTag(itemHandler::getStackInSlot, itemHandler.getSlots());
        this.addInventoryTag(stackOut, inventoryTag);
    }

    protected void makeFromVanillaInterface(IInventory tileEntity, ItemStack stackOut) {
        ListNBT inventoryTag = this.makeTag(tileEntity::getStackInSlot, tileEntity.getSizeInventory());
        this.addInventoryTag(stackOut, inventoryTag);
    }

    public void addInventoryTag(ItemStack stackOut, ListNBT containerItemTag) {
        CompoundNBT stackTag = stackOut.getOrCreateTag();
        stackTag.put(NBTConstants.INVENTORY, containerItemTag);
    }

    protected ListNBT makeTag(Function<Integer, ItemStack> provider, int size) {
        ListNBT containerItemTag = new ListNBT();

        for (int i = 0; i < size; i++) {
            ItemStack stackInSlot = provider.apply(i);
            CompoundNBT currentStackToNBT = stackInSlot.write(new CompoundNBT());
            containerItemTag.add(i, currentStackToNBT);
        }

        return containerItemTag;
    }

}
