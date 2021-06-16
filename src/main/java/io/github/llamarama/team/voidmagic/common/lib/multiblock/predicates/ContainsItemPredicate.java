package io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class ContainsItemPredicate extends TileEntityPredicate {

    private final ItemStack stack;
    private final boolean itemOnly;
    private final boolean nbt;

    public ContainsItemPredicate(TileEntityType<? extends TileEntity> tileEntityType, ItemStack stack, boolean itemOnly, boolean nbt) {
        super(tileEntityType);
        this.stack = stack;
        this.itemOnly = itemOnly;
        this.nbt = nbt;
    }

    public ContainsItemPredicate(TileEntityType<?> tileEntityType, Item item) {
        this(tileEntityType, item.getDefaultInstance(), true, false);
    }

    @Override
    public boolean test(World world, BlockPos blockPos) {
        boolean oldTest = super.test(world, blockPos);

        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity == null)
            return oldTest;

        if (tileEntity instanceof IInventory) {
            for (int i = 0; i < ((IInventory) tileEntity).getSizeInventory(); i++) {
                ItemStack stackInSlot = ((IInventory) tileEntity).getStackInSlot(i);

                if (testStacks(stackInSlot))
                    return true;
            }
        }

        AtomicBoolean out = new AtomicBoolean(false);
        LazyOptional<IItemHandler> itemCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

        itemCap.ifPresent((itemHandler) -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stackInSlot = itemHandler.getStackInSlot(i);

                if (this.testStacks(stackInSlot)) {
                    out.set(true);
                    break;
                }
            }
        });

        return out.get();
    }

    private boolean testStacks(ItemStack stackInSlot) {
        boolean areItemsSame = this.stack.isItemEqual(stackInSlot);
        if (this.itemOnly && areItemsSame)
            return true;
        else if (!this.itemOnly && !nbt && this.stack.getCount() == stackInSlot.getCount() && areItemsSame)
            return true;
        else return !this.itemOnly && nbt && this.stack.equals(stackInSlot, false);
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public Item getTargetItem() {
        return this.stack.getItem();
    }

}
