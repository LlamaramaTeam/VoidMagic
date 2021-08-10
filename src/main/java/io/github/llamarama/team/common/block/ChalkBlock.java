package io.github.llamarama.team.common.block;

import io.github.llamarama.team.api.block.properties.ChalkType;
import io.github.llamarama.team.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.common.register.ModBlocks;
import io.github.llamarama.team.common.register.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class ChalkBlock extends Block {

    private static final Property<ChalkType> CHALK_TYPE = ModBlockProperties.CHALK_TYPE;

    public ChalkBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(CHALK_TYPE, ChalkType.NONE));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0, 0, 0, 16, 1, 16);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
        boolean holdsItem = heldItem.getItem() == ModItems.GUIDE_BOOK;
        if (world.isClient) {
            return holdsItem
                    ? ActionResult.SUCCESS
                    : ActionResult.PASS;
        }

        if (holdsItem && state.getBlock() == ModBlocks.CHALK) {
            ChalkType chalkType = state.get(CHALK_TYPE);
            world.setBlockState(pos, state.with(CHALK_TYPE, chalkType.next()));
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState stateBelow = world.getBlockState(pos.down());
        return !world.isAir(pos.down()) && stateBelow.isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() :
                super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHALK_TYPE);
    }

}
