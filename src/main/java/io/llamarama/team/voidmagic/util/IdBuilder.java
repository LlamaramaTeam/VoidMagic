package io.llamarama.team.voidmagic.util;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.util.ResourceLocation;

public final class IdBuilder {

    public static ResourceLocation mod(String path) {
        return new ResourceLocation(VoidMagic.MOD_ID, path);
    }

    public static ResourceLocation mc(String path) {
        return new ResourceLocation("minecraft", path);
    }

}
