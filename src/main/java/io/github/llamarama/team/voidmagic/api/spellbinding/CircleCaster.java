package io.github.llamarama.team.voidmagic.api.spellbinding;

import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockType;

public interface CircleCaster {

    SpellbindingCircle getCircle();

    default MultiblockType multiblockType() {
        return this.getCircle().getMultiblockType();
    }

}
