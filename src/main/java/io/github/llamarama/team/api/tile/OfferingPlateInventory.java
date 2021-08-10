package io.github.llamarama.team.api.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface OfferingPlateInventory extends Inventory {

    DefaultedList<ItemStack> getStacks();

    @Override
    default int size() {
        return this.getStacks().size();
    }

    @Override
    default boolean isEmpty() {
        return this.getStacks().isEmpty();
    }

    @Override
    default ItemStack getStack(int slot) {
        return this.getStacks().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        ItemStack resultingStack = Inventories.splitStack(this.getStacks(), slot, amount);

        if (!resultingStack.isEmpty())
            this.markDirty();

        return resultingStack;
    }

    @Override
    default ItemStack removeStack(int slot) {
        ItemStack itemStack = Inventories.removeStack(this.getStacks(), slot);
        this.markDirty();
        return itemStack;
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        this.getStacks().set(slot, stack);

        if (stack.getCount() > this.getMaxCountPerStack())
            stack.setCount(this.getMaxCountPerStack());

        this.markDirty();
    }

    @Override
    default int getMaxCountPerStack() {
        return 1;
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    default void clear() {
        this.getStacks().clear();
        this.markDirty();
    }

}
