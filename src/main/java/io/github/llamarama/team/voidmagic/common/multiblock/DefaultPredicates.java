package io.github.llamarama.team.voidmagic.common.multiblock;

import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import io.github.llamarama.team.voidmagic.common.multiblock.predicates.BlockStatePredicate;
import io.github.llamarama.team.voidmagic.common.multiblock.predicates.BlockTagPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultPredicates {

    private DefaultPredicates() {
    }

    public static BlockPredicate match(Block block) {
        return (world, pos) -> world.getBlockState(pos).getBlock() == block;
    }

    public static BlockPredicate match(BlockState state) {
        return new BlockStatePredicate(state);
    }

    public static BlockPredicate contains(ItemStack stack) {
        return contains(stack, Optional.empty());
    }

    public static BlockPredicate isInTag(ITag<Block> tag) {
        return new BlockTagPredicate(tag);
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

    public static BlockPredicate any() {
        return (world, pos) -> true;
    }

}
