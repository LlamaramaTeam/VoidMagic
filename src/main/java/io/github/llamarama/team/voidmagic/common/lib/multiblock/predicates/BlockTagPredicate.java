package io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates;

import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTagPredicate implements BlockPredicate {

    private final ITag<Block> tag;

    public BlockTagPredicate(ITag<Block> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock().isIn(this.tag);
    }

    public ITag<Block> getTag() {
        return this.tag;
    }

    public BlockState getRandomState(Random random) {
        return this.tag.getRandomElement(random).getDefaultState();
    }

}
