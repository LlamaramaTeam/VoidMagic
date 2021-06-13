package io.github.llamarama.team.voidmagic.api.spellbinding;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;

public interface ICircleCaster {

    ISpellbindingCircle getCircle();

    void setCircle(ISpellbindingCircle circle);

    default IMultiblockType multiblockType() {
        return this.getCircle().multiblock();
    }

}
