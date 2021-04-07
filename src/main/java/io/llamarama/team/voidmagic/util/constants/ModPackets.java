package io.llamarama.team.voidmagic.util.constants;

import io.llamarama.team.voidmagic.util.IdBuilder;
import net.minecraft.util.ResourceLocation;

public enum ModPackets {

    ;

    private final ResourceLocation loc;
    private final int uniqueIndex;

    ModPackets(int uniqueIndex, String id) {
        this.uniqueIndex = uniqueIndex;
        this.loc = IdBuilder.mod(id);
    }

    public int getUniqueIndex() {
        return uniqueIndex;
    }

    public ResourceLocation getLoc() {
        return loc;
    }

}
