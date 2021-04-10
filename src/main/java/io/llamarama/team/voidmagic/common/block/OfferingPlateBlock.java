package io.llamarama.team.voidmagic.common.block;

import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
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
            return ActionResultType.PASS;
        }

        final AtomicReference<ActionResultType> returnVal = new AtomicReference<>();

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler) -> {
            if (worldIn.isRemote) {
                return;
            }

            ItemStack heldStack = player.getHeldItem(handIn);
            Item heldItem = heldStack.getItem();
            ItemStack inventoryStack = itemHandler.getStackInSlot(0);

            if (inventoryStack.isEmpty()) {
                itemHandler.insertItem(0, new ItemStack(heldItem), false);
                heldStack.shrink(1);

                returnVal.set(ActionResultType.SUCCESS);
            } else if (!inventoryStack.isEmpty()) {
                itemHandler.extractItem(0, 1, false);

                if (!player.inventory.addItemStackToInventory(inventoryStack)) {
                    worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventoryStack));
                }
                returnVal.set(ActionResultType.SUCCESS);
            }
        });

        return returnVal.get();
    }

}
