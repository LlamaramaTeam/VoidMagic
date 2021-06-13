package io.github.llamarama.team.voidmagic.common.lib.multiblock.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindable;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CircleMultiblock extends Multiblock implements ISpellbindable {

    public CircleMultiblock(IMultiblockType type, BlockPos center, MultiblockRotation rotation) {
        super(type, center, rotation);
    }

    @Override
    public void circleFormed(World world, BlockPos pos, BlockState state) {

    }

    public void getTiles() {
    }

    public void getBox() {

    }

}
