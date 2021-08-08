package com.github.llamarama.team.common.block;

import com.github.llamarama.team.api.block.properties.ModBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class PillarBlock extends Block implements Waterloggable {

    private static final BooleanProperty HAS_TOP = ModBlockProperties.HAS_TOP;
    private static final BooleanProperty HAS_BOTTOM = ModBlockProperties.HAS_BOTTOM;
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.createCuboidShape(1, 0, 1, 15, 16, 15),
            Block.createCuboidShape(0, 0, 0, 16, 3, 16),
            Block.createCuboidShape(0, 13, 0, 16, 16, 16)
    };

    public PillarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(HAS_BOTTOM, false).with(HAS_TOP, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean hasTop = state.get(HAS_TOP);
        boolean hasBottom = state.get(HAS_BOTTOM);

        if (!hasTop && hasBottom) {
            return VoxelShapes.union(SHAPES[0], SHAPES[2]);
        } else if (!hasBottom && hasTop) {
            return VoxelShapes.union(SHAPES[0], SHAPES[1]);
        } else if (hasBottom) {
            return SHAPES[0];
        } else {
            return VoxelShapes.union(SHAPES[0], VoxelShapes.union(SHAPES[1], SHAPES[2]));
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            BlockState down = world.getBlockState(pos.down());
            BlockState up = world.getBlockState(pos.up());

            if (down.getBlock() instanceof PillarBlock) {
                world.setBlockState(pos, state.with(HAS_BOTTOM, true));
                world.setBlockState(pos.down(), down.with(HAS_TOP, true));
            }

            if (up.getBlock() instanceof PillarBlock) {
                world.setBlockState(pos, state.with(HAS_TOP, true));
                world.setBlockState(pos.up(), up.with(HAS_BOTTOM, true));
            }
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (!world.isClient()) {
            BlockState down = world.getBlockState(pos.down());
            BlockState up = world.getBlockState(pos.up());
            if (down.getBlock() instanceof PillarBlock) {
                world.setBlockState(pos.down(), down.with(HAS_TOP, false), Block.NOTIFY_ALL);
            }

            if (up.getBlock() instanceof PillarBlock) {
                world.setBlockState(pos.up(), up.with(HAS_BOTTOM, false), Block.NOTIFY_ALL);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        this.updateForChange(world, pos);
    }

    protected void updateForChange(World world, BlockPos pos) {
        if (!world.isClient) {
            BlockState currentState = world.getBlockState(pos);
            if (currentState.getBlock() instanceof PillarBlock) {
                BlockState down = world.getBlockState(pos.down());
                BlockState up = world.getBlockState(pos.up());

                int updateFlags = Block.NOTIFY_ALL;

                if (down.getBlock() instanceof PillarBlock && !currentState.get(HAS_BOTTOM)) {
                    world.setBlockState(pos, currentState.with(HAS_BOTTOM, true), updateFlags);

                    if (!down.get(HAS_TOP)) {
                        world.setBlockState(pos.down(), down.with(HAS_TOP, true), updateFlags);
                    }
                } else if (!(down.getBlock() instanceof PillarBlock)) {
                    world.setBlockState(pos, currentState.with(HAS_BOTTOM, false), updateFlags);
                }

                if (up.getBlock() instanceof PillarBlock && !currentState.get(HAS_TOP)) {
                    world.setBlockState(pos, currentState.with(HAS_TOP, true), updateFlags);

                    if (!up.get(HAS_BOTTOM)) {
                        world.setBlockState(pos.up(), up.with(HAS_BOTTOM, true));
                    }
                } else if (!(up.getBlock() instanceof PillarBlock)) {
                    world.setBlockState(pos, currentState.with(HAS_TOP, false), updateFlags);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

        return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_BOTTOM, HAS_TOP, WATERLOGGED);
    }

}
