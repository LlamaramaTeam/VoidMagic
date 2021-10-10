package io.github.llamarama.team.voidmagic.common.block;

import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.common.block_entity.ScrollBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class ScrollBlock extends HorizontalFacingBlock implements BlockEntityProvider {

    public static final BooleanProperty OPEN = ModBlockProperties.OPEN;
    public static final DirectionProperty HORIZONTAL_FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape SCROLL_WEST = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(4, 0, 0, 9, 5, 16),
            Block.createCuboidShape(5, 1, -2, 8, 4, 18),
            BooleanBiFunction.OR);
    private static final VoxelShape SCROLL_NORTH = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(0, 0, 4, 16, 5, 9),
            Block.createCuboidShape(-2, 1, 5, 18, 4, 8),
            BooleanBiFunction.OR);
    private static final VoxelShape SCROLL_EAST = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(7, 0, 0, 12, 5, 16),
            Block.createCuboidShape(8, 1, -2, 11, 4, 18),
            BooleanBiFunction.OR);
    private static final VoxelShape SCROLL_SOUTH = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(0, 0, 7, 16, 5, 12),
            Block.createCuboidShape(-2, 1, 8, 18, 4, 11),
            BooleanBiFunction.OR);
    private static final VoxelShape SCROLL_WEST_OPEN = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(-16, 0, -2, -13, 3, 18),
            Block.createCuboidShape(-13, 0, 0, 32, 0.25d, 16),
            BooleanBiFunction.OR);
    private static final VoxelShape SCROLL_NORTH_OPEN = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(-2, 0, -16, 18, 3, -13),
            Block.createCuboidShape(0, 0, -13, 16, 0.25d, 32),
            BooleanBiFunction.OR);
    private static final VoxelShape SCROLL_EAST_OPEN = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(29, 0, -2, 32, 3, 18),
            Block.createCuboidShape(-16, 0, 0, 29, 0.25d, 16),
            BooleanBiFunction.OR);
    private static final VoxelShape SCROLL_SOUTH_OPEN = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(-2, 0, 29, 18, 3, 32),
            Block.createCuboidShape(0, 0, -16, 16, 0.25d, 29),
            BooleanBiFunction.OR);

    public ScrollBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(OPEN, false)
                .with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(HORIZONTAL_FACING);
        Boolean isOpen = state.get(OPEN);

        Pair<VoxelShape, VoxelShape> closedAndOpenShapes;

        switch (facing) {
            case EAST -> closedAndOpenShapes = new Pair<>(SCROLL_EAST, SCROLL_EAST_OPEN);
            case SOUTH -> closedAndOpenShapes = new Pair<>(SCROLL_SOUTH, SCROLL_SOUTH_OPEN);
            case WEST -> closedAndOpenShapes = new Pair<>(SCROLL_WEST, SCROLL_WEST_OPEN);
            default -> closedAndOpenShapes = new Pair<>(SCROLL_NORTH, SCROLL_NORTH_OPEN);
        }

        return isOpen ? closedAndOpenShapes.getRight() : closedAndOpenShapes.getLeft();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockState newState = state.with(OPEN, !state.get(OPEN));

        if (!world.isClient) {
            world.setBlockState(pos, newState);
        }

        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING, ctx.getPlayerLookDirection().rotateYCounterclockwise());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(ModBlockProperties.OPEN)) {
            Direction direction = state.get(HORIZONTAL_FACING);
            BlockPos left = pos.offset(direction, -1).down();
            BlockPos right = pos.offset(direction, 1).down();

            boolean isLeftFull = world.getBlockState(left).isSideSolidFullSquare(world, right, Direction.UP);
            boolean isRightFull = world.getBlockState(right).isSideSolidFullSquare(world, right, Direction.UP);
            boolean positionDown = world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.UP);

            return isLeftFull && isRightFull && positionDown;
        } else
            return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ScrollBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (beWorld, pos, cachedState, blockEntity) -> {
            if (!world.isClient && blockEntity instanceof ScrollBlockEntity scrollBlockEntity) {
                if (cachedState.get(OPEN) && cachedState.canPlaceAt(beWorld, pos)) {
                    beWorld.removeBlock(pos, false);
                }

                if (!scrollBlockEntity.isCrafting() && scrollBlockEntity.getCircle() == null) {
                    scrollBlockEntity.findCircle();
                } else if (scrollBlockEntity.getCraftingTick() == 0) {
                    scrollBlockEntity.finishCrafting();
                } else if (scrollBlockEntity.getCircle() != null && scrollBlockEntity.isCrafting()) {
                    if (scrollBlockEntity.multiblockType().existsAt(pos, world)) {
                        scrollBlockEntity.progressCrafting();
                    } else {
                        scrollBlockEntity.cancelCrafting();
                    }
                }
            }
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ?
                Blocks.AIR.getDefaultState() :
                super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN, HORIZONTAL_FACING);
    }

}
