package io.github.llamarama.team.voidmagic.common.lib.multiblock;

import io.github.llamarama.team.voidmagic.api.multiblock.PositionPredicate;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockStatePredicate;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockTagPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultPredicates {

    private DefaultPredicates() {

    }

    public static PositionPredicate match(Block block) {
        return (world, pos) -> world.getBlockState(pos).getBlock() == block;
    }

    public static PositionPredicate match(BlockState state) {
        return new BlockStatePredicate(state);
    }

    public static PositionPredicate contains(ItemStack stack) {
        return contains(stack, Optional.empty());
    }

    public static PositionPredicate isInTag(Tag<Block> tag) {
        return new BlockTagPredicate(tag);
    }

    public static PositionPredicate contains(ItemStack stack, Optional<BlockState> state) {
        return (world, pos) -> {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            BlockState worldBlockState = world.getBlockState(pos);

            final AtomicBoolean ret = new AtomicBoolean(false);
            state.ifPresent((blockState) -> ret.set(blockState.equals(worldBlockState)));

            if (ret.get() && tileEntity != null) {
                if (tileEntity instanceof Inventory inventory) {
                    for (int i = 0; i < inventory.size(); i++) {
                        ItemStack stackInSlot = inventory.getStack(i);
                        boolean isStackEqual = ItemStack.areEqual(stackInSlot, stack);

                        if (isStackEqual) {
                            ret.set(true);
                            break;
                        }
                    }
                }
            }


            return ret.get();
        };
    }


}
