package io.github.llamarama.team.voidmagic.common.tag;

import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.NotNull;

public final class ModTags {

    public static final class Blocks {

        public static final ITag.INamedTag<Block> TOFAL_BLOCKS = getBlockINamedTag("tofal_blocks");
        public static final ITag.INamedTag<Block> WITHERED_STONE_BLOCKS = getBlockINamedTag("withered_stone_blocks");

        @NotNull
        private static ITag.INamedTag<Block> getBlockINamedTag(String id) {
            return BlockTags.makeWrapperTag(IdBuilder.mod(id).toString());
        }

    }

    public static final class Items {

        public static final ITag.INamedTag<Item> TOFAL_BLOCKS = getItemINamedTag("tofal_blocks");
        public static final ITag.INamedTag<Item> WITHERED_STONE_BLOCKS = getItemINamedTag("withered_stone_blocks");

        @NotNull
        private static ITag.INamedTag<Item> getItemINamedTag(String id) {
            return ItemTags.makeWrapperTag(IdBuilder.mod(id).toString());
        }

    }

}
