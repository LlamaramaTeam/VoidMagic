package io.github.llamarama.team.voidmagic.common.block;

import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindable;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.common.tile.ScrollTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import static io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties.OPEN;

public class ScrollBlock extends HorizontalBlock implements ISpellbindable {

    public static final VoxelShape SCROLL_WEST = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(4, 0, 0, 9, 5, 16),
            Block.makeCuboidShape(5, 1, -2, 8, 4, 18),
            IBooleanFunction.OR);
    public static final VoxelShape SCROLL_NORTH = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(0, 0, 4, 16, 5, 9),
            Block.makeCuboidShape(-2, 1, 5, 18, 4, 8),
            IBooleanFunction.OR);
    public static final VoxelShape SCROLL_EAST = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(7, 0, 0, 12, 5, 16),
            Block.makeCuboidShape(8, 1, -2, 11, 4, 18),
            IBooleanFunction.OR);
    public static final VoxelShape SCROLL_SOUTH = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(0, 0, 7, 16, 5, 12),
            Block.makeCuboidShape(-2, 1, 8, 18, 4, 11),
            IBooleanFunction.OR);
    public static final VoxelShape SCROLL_WEST_OPEN = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(-16, 0, -2, -13, 3, 18),
            Block.makeCuboidShape(-13, 0, 0, 32, 0.25d, 16),
            IBooleanFunction.OR);
    public static final VoxelShape SCROLL_NORTH_OPEN = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(-2, 0, -16, 18, 3, -13),
            Block.makeCuboidShape(0, 0, -13, 16, 0.25d, 32),
            IBooleanFunction.OR);
    public static final VoxelShape SCROLL_EAST_OPEN = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(29, 0, -2, 32, 3, 18),
            Block.makeCuboidShape(-16, 0, 0, 29, 0.25d, 16),
            IBooleanFunction.OR);
    public static final VoxelShape SCROLL_SOUTH_OPEN = VoxelShapes.combineAndSimplify(
            Block.makeCuboidShape(-2, 0, 29, 18, 3, 32),
            Block.makeCuboidShape(0, 0, -16, 16, 0.25d, 29),
            IBooleanFunction.OR);

    public ScrollBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState()
                .with(OPEN, false)
                .with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(HORIZONTAL_FACING);
        boolean isOpen = state.get(OPEN);

        Pair<VoxelShape, VoxelShape> closedAndOpen;
        switch (direction) {
            case EAST:
                closedAndOpen = Pair.of(SCROLL_EAST, SCROLL_EAST_OPEN);
                break;
            case SOUTH:
                closedAndOpen = Pair.of(SCROLL_SOUTH, SCROLL_SOUTH_OPEN);
                break;
            case WEST:
                closedAndOpen = Pair.of(SCROLL_WEST, SCROLL_WEST_OPEN);
                break;
            default:
                closedAndOpen = Pair.of(SCROLL_NORTH, SCROLL_NORTH_OPEN);
                break;
        }

        return isOpen ? closedAndOpen.getRight() : closedAndOpen.getLeft();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        BlockState newState = state.with(OPEN, !state.get(OPEN));
        if (!worldIn.isRemote && newState.isValidPosition(worldIn, pos)) {
            worldIn.setBlockState(pos, newState);
        }

        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING,
                context.getPlacementHorizontalFacing().rotateY().rotateY().rotateY());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(OPEN, HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ScrollTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (state.get(ModBlockProperties.OPEN)) {
            Direction direction = state.get(HorizontalBlock.HORIZONTAL_FACING);
            BlockPos left = pos.offset(direction, -1).down();
            BlockPos right = pos.offset(direction, 1).down();

            boolean isLeftFull = worldIn.getBlockState(left).isSolidSide(worldIn, right, Direction.UP);
            boolean isRightFull = worldIn.getBlockState(right).isSolidSide(worldIn, right, Direction.UP);
            boolean positionDown = worldIn.getBlockState(pos.down()).isSolidSide(worldIn, pos, Direction.UP);

            return isLeftFull && isRightFull && positionDown;
        } else
            return worldIn.getBlockState(pos.down()).isSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos)
                ? Blocks.AIR.getDefaultState()
                : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void circleFormed(World world, BlockPos pos, BlockState state) {
        if (world.isRemote)
            return;

        world.setBlockState(pos, ModBlocks.CHALK.get().getDefaultState());
    }

}
