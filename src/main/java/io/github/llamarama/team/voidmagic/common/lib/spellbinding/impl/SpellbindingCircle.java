package io.github.llamarama.team.voidmagic.common.lib.spellbinding.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindable;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.CircleMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

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
    public Supplier<? extends CircleMultiblock> multiblock(BlockPos center, World world) {
        return () -> new CircleMultiblock(this.type, center, world);
    }

    @Override
    public ISpellbindable getResult() {
        return this.result;
    }

    @Override
    public int getCraftingTime() {
        return this.craftingTime;
    }

    @Override
    public IMultiblockType getMultiblockType() {
        return this.type;
    }

}
