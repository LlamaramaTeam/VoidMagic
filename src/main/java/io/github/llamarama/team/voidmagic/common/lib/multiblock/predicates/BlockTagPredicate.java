package io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates;

import io.github.llamarama.team.voidmagic.api.multiblock.PositionPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public record BlockTagPredicate(Tag<Block> tag) implements PositionPredicate {

    @Override
    public boolean checkPos(World world, BlockPos pos) {
        return world.getBlockState(pos).isIn(this.tag);
    }

    public Tag<Block> getTag() {
        return tag;
    }

    public BlockState getRandomState(Random random) {
        return this.tag.getRandom(random).getDefaultState();
    }

}
