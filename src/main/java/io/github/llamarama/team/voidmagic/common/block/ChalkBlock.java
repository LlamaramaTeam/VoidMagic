package io.github.llamarama.team.voidmagic.common.block;

import io.github.llamarama.team.voidmagic.common.block.util.ChalkType;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class ChalkBlock extends Block {

    public static final EnumProperty<ChalkType> TYPE = EnumProperty.create("type", ChalkType.class);

    public ChalkBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(TYPE, ChalkType.NONE));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0, 0, 0, 16, 1, 16);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        boolean holdsItem = heldItem.getItem() == ModItems.GUIDE_BOOK.get();
        if (worldIn.isRemote) {
            return holdsItem
                    ? ActionResultType.SUCCESS
                    : ActionResultType.PASS;
        }

        if (holdsItem && state.getBlock() == ModBlocks.CHALK.get()) {
            ChalkType chalkType = state.get(TYPE);
            worldIn.setBlockState(pos, state.with(TYPE, chalkType.next()));
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockState worldInBlockState = worldIn.getBlockState(pos.down());
        return !worldIn.isAirBlock(pos.down()) && worldInBlockState.isSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

}
