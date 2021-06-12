package io.github.llamarama.team.voidmagic.api.spellbinding;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;

public interface ISpellbindingCircle {

    IMultiblockType multiblock();

    ISpellBindable getResult();

}
