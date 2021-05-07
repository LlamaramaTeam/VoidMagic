package io.github.llamarama.team.voidmagic.common.multiblock;

import io.github.llamarama.team.voidmagic.common.impl.MultiblockType;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;

public final class ModMultiblocks {

    public static final MultiblockType<?> TYPE = MultiblockType.Builder
            .create(IdBuilder.mod("test"), true)
            .withSize(7, 1, 7)
            .define('X', DefaultPredicates.isInTag(BlockTags.WOOL))
            .define('C', DefaultPredicates.match(Blocks.COAL_BLOCK))
            .withPlacementOffset(-3, -1, -3)
            .pattern(new String[][]{
                    {
                            "XXXXXXX",
                            "XXXXXXX",
                            "XXXXXXX",
                            "XXXCXXX",
                            "XXXXXXX",
                            "XXXXXXX",
                            "XXXXXXX"
                    }
            }).build();

}
