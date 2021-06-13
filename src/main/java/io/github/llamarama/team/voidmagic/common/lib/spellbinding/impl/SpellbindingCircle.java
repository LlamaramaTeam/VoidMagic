package io.github.llamarama.team.voidmagic.common.lib.spellbinding.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindable;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;

public class SpellbindingCircle implements ISpellbindingCircle {

    private final IMultiblockType type;
    private final ISpellbindable result;
    private final int craftingTime;

    public SpellbindingCircle(IMultiblockType type, ISpellbindable result) {
        this(type, result, 200);
    }

    public SpellbindingCircle(IMultiblockType type, ISpellbindable result, int craftingTime) {
        this.type = type;
        this.result = result;
        this.craftingTime = craftingTime;
    }

    @Override
    public IMultiblockType multiblock() {
        return this.type;
    }

    @Override
    public ISpellbindable getResult() {
        return this.result;
    }

    @Override
    public int getCraftingTime() {
        return this.craftingTime;
    }

}
