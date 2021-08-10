package io.github.llamarama.team.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

import java.util.Optional;

public interface InventoryUtils {

    String INVENTORY_KEY = "Inventory";
    String SLOT_KEY = "Slot";

    static void readInventory(NbtCompound tag, DefaultedList<ItemStack> stacks) {
        NbtList list = (NbtList) tag.get(INVENTORY_KEY);

        Optional.ofNullable(list).ifPresentOrElse((serializedStacks) -> {
            for (int i = 0; i < serializedStacks.size(); i++) {
                NbtCompound currentStackTag = serializedStacks.getCompound(i);

                byte slot = currentStackTag.getByte(SLOT_KEY);
                ItemStack stack = ItemStack.fromNbt(currentStackTag);

                stacks.set(slot, stack);
            }
        }, () -> {
            throw new IllegalStateException("Could not parse item stack tag.");
        });
    }

    static void writeInventory(NbtCompound tag, DefaultedList<ItemStack> stacks) {
        NbtList serializedStacks = new NbtList();

        for (int i = 0; i < stacks.size(); i++) {
            NbtCompound currentTag = new NbtCompound();
            ItemStack slotStack = stacks.get(i);
            currentTag.putByte(SLOT_KEY, (byte) i);
            slotStack.writeNbt(currentTag);

            serializedStacks.add(currentTag);
        }

        tag.put(INVENTORY_KEY, serializedStacks);
    }

}
