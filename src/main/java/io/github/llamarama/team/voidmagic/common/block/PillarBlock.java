package io.github.llamarama.team.voidmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;

public class PillarBlock extends Block implements IWaterLoggable {

    public static final BooleanProperty HAS_TOP = BooleanProperty.create("has_top");
    public static final BooleanProperty HAS_BOTTOM = BooleanProperty.create("has_bottom");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.makeCuboidShape(1, 0, 1, 15, 16, 15),
            Block.makeCuboidShape(0, 0, 0, 16, 3, 16),
            Block.makeCuboidShape(0, 13, 0, 16, 16, 16)
    };

    public PillarBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(HAS_TOP, false).with(HAS_BOTTOM, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        boolean hasTop = state.get(HAS_TOP);
        boolean hasBottom = state.get(HAS_BOTTOM);

        if (!hasTop && hasBottom) {
            return VoxelShapes.or(SHAPES[0], SHAPES[2]);
        } else if (!hasBottom && hasTop) {
            return VoxelShapes.or(SHAPES[0], SHAPES[1]);
        } else if (hasBottom) {
            return SHAPES[0];
        } else {
            return VoxelShapes.or(SHAPES[0], VoxelShapes.or(SHAPES[1], SHAPES[2]));
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HAS_TOP, HAS_BOTTOM, WATERLOGGED);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!worldIn.isRemote()) {
            BlockState down = worldIn.getBlockState(pos.down());
            BlockState up = worldIn.getBlockState(pos.up());

            if (down.getBlock() instanceof PillarBlock) {
                worldIn.setBlockState(pos, state.with(HAS_BOTTOM, true));
                worldIn.setBlockState(pos.down(), down.with(HAS_TOP, true));
            }

            if (up.getBlock() instanceof PillarBlock) {
                worldIn.setBlockState(pos, state.with(HAS_TOP, true));
                worldIn.setBlockState(pos.up(), up.with(HAS_BOTTOM, true));
            }
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (!world.isRemote()) {
            BlockState down = world.getBlockState(pos.down());
            BlockState up = world.getBlockState(pos.up());
            if (down.getBlock() instanceof PillarBlock) {
                world.setBlockState(pos.down(), down.with(HAS_TOP, false));
            }

            if (up.getBlock() instanceof PillarBlock) {
                world.setBlockState(pos.up(), up.with(HAS_BOTTOM, false));
            }
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        this.updateForChange(worldIn, pos);
    }

    protected void updateForChange(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            BlockState currentState = worldIn.getBlockState(pos);
            if (currentState.getBlock() instanceof PillarBlock) {
                BlockState down = worldIn.getBlockState(pos.down());
                BlockState up = worldIn.getBlockState(pos.up());

                int updateFlags = Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.UPDATE_NEIGHBORS;

                if (down.getBlock() instanceof PillarBlock && !currentState.get(HAS_BOTTOM)) {
                    worldIn.setBlockState(pos, currentState.with(HAS_BOTTOM, true), updateFlags);

                    if (!down.get(HAS_TOP)) {
                        worldIn.setBlockState(pos.down(), down.with(HAS_TOP, true), updateFlags);
                    }
                } else if (!(down.getBlock() instanceof PillarBlock)) {
                    worldIn.setBlockState(pos, currentState.with(HAS_BOTTOM, false), updateFlags);
                }

                if (up.getBlock() instanceof PillarBlock && !currentState.get(HAS_TOP)) {
                    worldIn.setBlockState(pos, currentState.with(HAS_TOP, true), updateFlags);

                    if (!up.get(HAS_BOTTOM)) {
                        worldIn.setBlockState(pos.up(), up.with(HAS_BOTTOM, true));
                    }
                } else if (!(up.getBlock() instanceof PillarBlock)) {
                    worldIn.setBlockState(pos, currentState.with(HAS_TOP, false), updateFlags);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getPos());

        return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

}
