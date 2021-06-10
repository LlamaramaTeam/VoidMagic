package io.github.llamarama.team.voidmagic.common.multiblock.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import net.minecraft.util.math.BlockPos;

public class Multiblock implements IMultiblock {

    private IMultiblockType type;
    private BlockPos center;

    public Multiblock(IMultiblockType type, BlockPos center) {
        this.type = type;
        this.center = center;
    }

    @Override
    public IMultiblockType getType() {
        return this.type;
    }

    @Override
    public void setType(IMultiblockType type) {
        this.type = type;
    }

    @Override
    public BlockPos getPos() {
        return this.center;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.center = pos;
    }

}
