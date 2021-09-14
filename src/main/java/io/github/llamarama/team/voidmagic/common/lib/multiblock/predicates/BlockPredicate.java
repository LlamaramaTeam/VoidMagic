package io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates;

import io.github.llamarama.team.voidmagic.api.multiblock.PositionPredicate;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record BlockPredicate(Block block) implements PositionPredicate {

    @Override
    public boolean checkPos(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(this.block);
    }

}
