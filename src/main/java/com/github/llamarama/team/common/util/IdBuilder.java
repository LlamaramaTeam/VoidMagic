package com.github.llamarama.team.common.util;

import com.github.llamarama.team.VoidMagic;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface IdBuilder {

    @NotNull
    static Identifier of(String id) {
        return new Identifier(VoidMagic.MOD_ID, id);
    }

}
