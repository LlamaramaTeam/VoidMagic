package io.github.llamarama.team.voidmagic.api.spellbinding;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;

public interface ISpellbindingCircle {

    IMultiblockType multiblock();

    ISpellbindable getResult();

    int getCraftingTime();

}
