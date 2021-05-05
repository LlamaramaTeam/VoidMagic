package io.github.llamarama.team.voidmagic.common.multiblock;

import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DefaultPredicates {

    private DefaultPredicates() {
    }

    public static BlockPredicate match(Block block) {
        return match(block.getDefaultState());
    }

    public static BlockPredicate match(BlockState state) {
        return (world, pos) -> world.getBlockState(pos).equals(state);
    }

    public static BlockPredicate contains(ItemStack stack) {
        return contains(stack, Optional.empty());
    }

    public static BlockPredicate isInTag(ITag<Block> tag) {
        return (world, pos) -> {
            BlockState state = world.getBlockState(pos);

            return state.getBlock().isIn(tag);
        };
    }

    public static BlockPredicate contains(ItemStack stack, Optional<BlockState> state) {
        return (world, pos) -> {
            TileEntity tileEntity = world.getTileEntity(pos);
            BlockState worldBlockState = world.getBlockState(pos);

            final AtomicBoolean ret = new AtomicBoolean(false);
            state.ifPresent((blockState) -> ret.set(blockState.equals(worldBlockState)));

            if (ret.get() && tileEntity != null) {
                if (tileEntity instanceof IInventory) {
                    for (int i = 0; i < ((IInventory) tileEntity).getSizeInventory(); i++) {
                        ItemStack stackInSlot = ((IInventory) tileEntity).getStackInSlot(i);
                        boolean isStackEqual = stackInSlot.equals(stack, false);

                        if (isStackEqual) {
                            ret.set(true);
                            break;
                        }
                    }
                }

                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler) -> {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack stackInSlot = itemHandler.getStackInSlot(i);

                        boolean itemEqual = stackInSlot.isItemEqual(stack);

                        if (itemEqual) {
                            ret.set(true);
                            break;
                        }
                    }
                });
            }


            return ret.get();
        };
    }

}
