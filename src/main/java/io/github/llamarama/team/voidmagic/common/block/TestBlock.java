package io.github.llamarama.team.voidmagic.common.block;

import io.github.llamarama.team.voidmagic.common.multiblock.ModMultiblocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class TestBlock extends Block {

    public TestBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (ModMultiblocks.FANCY.existsAt(pos, worldIn)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }

        return ActionResultType.SUCCESS;
    }

}
