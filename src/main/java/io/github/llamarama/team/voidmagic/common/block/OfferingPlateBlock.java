package io.github.llamarama.team.voidmagic.common.block;

import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class OfferingPlateBlock extends PlateBlock {

    public OfferingPlateBlock(Properties properties) {
        super(ModBlocks.WITHERED_STONE.get(), properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OfferingPlateTileEntity();
    }


    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (!(tileEntity instanceof OfferingPlateTileEntity)) {
            return ActionResultType.FAIL;
        }

        if (!worldIn.isRemote) {
            ((OfferingPlateTileEntity) tileEntity).interact((ServerPlayerEntity) player);
            worldIn.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity == null) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }

        LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

        capability.ifPresent((itemHandler) -> {
            int slots = itemHandler.getSlots();
            for (int i = 0; i < slots; i++) {
                ItemStack stackInSlot = itemHandler.getStackInSlot(i);

                world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stackInSlot));
            }
        });
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof OfferingPlateTileEntity)) {
            return super.getPickBlock(state, target, world, pos, player);
        }

        final AtomicReference<ItemStack> result = new AtomicReference<>();
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler) -> {
            ItemStack copy = itemHandler.getStackInSlot(0).copy();
            result.set(copy);
        });

        return result.get();
    }

}
