package io.github.llamarama.team.voidmagic.common.lib.spellbinding.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellBindable;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;

public class SpellbindingCircle implements ISpellbindingCircle {

    protected SpellbindingCircle(IMultiblockType type, ISpellBindable result) {

    }

    @Override
    public IMultiblockType multiblock() {
        return null;
    }

    @Override
    public ISpellBindable getResult() {
        return null;
    }

    public static class Builder {

    }

}
