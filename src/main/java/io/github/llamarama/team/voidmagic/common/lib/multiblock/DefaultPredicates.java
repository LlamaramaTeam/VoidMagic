package io.github.llamarama.team.voidmagic.common.lib.multiblock;

import io.github.llamarama.team.voidmagic.api.multiblock.PositionPredicate;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockPredicate;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockStatePredicate;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockTagPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;

import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultPredicates {

    private DefaultPredicates() {

    }

    public static PositionPredicate match(Block block) {
        return new BlockPredicate(block);
    }

    public static PositionPredicate match(BlockState state) {
        return new BlockStatePredicate(state);
    }

    public static PositionPredicate isInTag(Tag<Block> tag) {
        return new BlockTagPredicate(tag);
    }

    public static PositionPredicate contains(ItemStack stack, BlockState... states) {
        return (world, pos) -> {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            BlockState worldBlockState = world.getBlockState(pos);

            final AtomicBoolean ret = new AtomicBoolean(false);
            if (states.length > 0) {
                for (BlockState state : states) {
                    if (state.equals(worldBlockState)) {
                        ret.set(true);
                        break;
                    }
                }
            }

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
