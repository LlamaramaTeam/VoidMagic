package io.llamarama.team.voidmagic.util;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.util.ResourceLocation;

public final class IdBuilder {

    public static ResourceLocation mod(String path) {
        return new ResourceLocation(VoidMagic.MODID, path);
    }

    public static ResourceLocation mc(String path) {
        return new ResourceLocation("minecraft", path);
    }

}
