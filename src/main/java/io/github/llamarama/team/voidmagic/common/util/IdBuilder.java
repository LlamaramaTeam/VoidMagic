package io.github.llamarama.team.voidmagic.common.util;

import io.github.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface IdBuilder {

    @NotNull
    static Identifier of(String id) {
        return new Identifier(VoidMagic.MOD_ID, id);
    }

}
