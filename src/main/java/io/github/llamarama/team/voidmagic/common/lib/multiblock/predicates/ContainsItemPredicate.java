package io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainsItemPredicate extends BlockEntityPredicate {

    private final ItemStack stack;
    private final boolean itemOnly;
    private final boolean nbt;

    public ContainsItemPredicate(BlockEntityType<?> type, Item item) {
        this(type, item.getDefaultStack(), true, false);
    }

    public ContainsItemPredicate(BlockEntityType<?> type, ItemStack stack, boolean itemOnly, boolean nbt) {
        super(type);
        this.stack = stack;
        this.itemOnly = itemOnly;
        this.nbt = nbt;
    }

    @Override
    public boolean checkPos(World world, BlockPos pos) {
        boolean oldCheck = super.checkPos(world, pos);

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity == null)
            return oldCheck;

        if (blockEntity instanceof Inventory inventory) {
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stackInSlot = inventory.getStack(i);

                if (this.testStack(stackInSlot)) {
                    return oldCheck;
                }
            }
        }

        return oldCheck;
    }

    private boolean testStack(ItemStack stackInSlot) {
        boolean areItemsSame = this.stack.isItemEqual(stackInSlot);
        if (this.itemOnly && areItemsSame) {
            return true;
        } else if (!this.itemOnly && !nbt && this.stack.getCount() == stackInSlot.getCount() && areItemsSame) {
            return true;
        } else return !this.itemOnly && nbt && ItemStack.areEqual(stackInSlot, this.stack);
    }

}
