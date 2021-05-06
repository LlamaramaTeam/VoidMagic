package io.github.llamarama.team.voidmagic.common.multiblock;

import io.github.llamarama.team.voidmagic.common.impl.MultiblockType;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.minecraft.tags.BlockTags;

public final class ModMultiblocks {

    public static final MultiblockType<?> TYPE = MultiblockType.Builder
            .create(IdBuilder.mod("test"), true)
            .withSize(4, 3, 5)
            .define('X', DefaultPredicates.isInTag(BlockTags.WOOL))
            .pattern(new String[][]{
                    {
                            " XX ",
                            " XX ",
                            " XX ",
                            " XX ",
                            " XX "
                    },
                    {
                            "XXXX",
                            "XXXX",
                            "XXXX",
                            "XXXX",
                            "XXXX"
                    },
                    {
                            "XXXX",
                            "XXXX",
                            "XXXX",
                            "XXXX",
                            "XXXX"
                    }
            }).build();

}
