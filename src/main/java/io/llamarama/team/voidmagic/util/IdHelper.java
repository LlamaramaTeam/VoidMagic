package io.llamarama.team.voidmagic.util;

import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class IdHelper {

    public static String getNonNullPath(Block block) {
        ResourceLocation loc = block.getRegistryName();
        if (loc != null) {
            return loc.getPath();
        }

        return StringConstants.EMPTY;
    }

    public static String getNonNullPath(Item item) {
        ResourceLocation loc = item.getRegistryName();
        if (loc != null) {
            return loc.getPath();
        }

        return StringConstants.EMPTY;
    }

    private IdHelper() {
    }

}
