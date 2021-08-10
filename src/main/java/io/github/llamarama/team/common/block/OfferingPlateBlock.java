package io.github.llamarama.team.common.block;

import io.github.llamarama.team.common.register.ModBlockEntityTypes;
import io.github.llamarama.team.common.tile.OfferingPlateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class OfferingPlateBlock extends PlateBlock {

    public OfferingPlateBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.OFFERING_PLATE.instantiate(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!(blockEntity instanceof OfferingPlateBlockEntity)) {
            return ActionResult.FAIL;
        }

        if (!world.isClient) {
            ((OfferingPlateBlockEntity) blockEntity).interact((ServerPlayerEntity) player);
        }

        return ActionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        Optional.ofNullable(world.getBlockEntity(pos)).ifPresent((blockEntity) -> {
            if (blockEntity instanceof Inventory)
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
        });

        world.updateComparators(pos, this);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        final AtomicReference<ItemStack> resultingStack = new AtomicReference<>(this.asItem().getDefaultStack());

        Optional.ofNullable(world.getBlockEntity(pos)).ifPresent((blockEntity) -> {
            if (blockEntity instanceof OfferingPlateBlockEntity) {
                resultingStack.set(((OfferingPlateBlockEntity) blockEntity).getStack(0).copy());
            }
        });

        return resultingStack.get();
    }

}
