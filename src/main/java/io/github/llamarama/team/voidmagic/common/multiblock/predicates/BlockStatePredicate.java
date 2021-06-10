package io.github.llamarama.team.voidmagic.common.multiblock.predicates;

import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStatePredicate implements BlockPredicate {

    private final BlockState state;

    public BlockStatePredicate(BlockState state) {
        this.state = state;
    }

    @Override
    public boolean test(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).equals(this.state);
    }

    public BlockState getState() {
        return this.state;
    }

}
