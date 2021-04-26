package io.github.llamarama.team.voidmagic.util;

import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTHelper {

    public static void writeItemStack(CompoundNBT tag, ItemStack stack) {
        tag.putInt(NBTConstants.ITEMSTACK_SIZE, stack.getCount());
        tag.putString(NBTConstants.ITEM_ID, IdHelper.getIdString(stack.getItem()));
        CompoundNBT stackTag = stack.getTag();

        if (stackTag != null) {
            tag.put(NBTConstants.EXTRA_NBT, stackTag);
        }
    }

    public static void readItemStack() {

    }

}
