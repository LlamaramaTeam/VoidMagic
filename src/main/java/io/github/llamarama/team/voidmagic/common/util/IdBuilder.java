package io.github.llamarama.team.voidmagic.common.util;

import io.github.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.util.ResourceLocation;

public final class IdBuilder {

    public static ResourceLocation mod(String path) {
        return new ResourceLocation(VoidMagic.MOD_ID, path.toLowerCase());
    }

    public static ResourceLocation mc(String path) {
        return new ResourceLocation("minecraft", path.toLowerCase());
    }

}
