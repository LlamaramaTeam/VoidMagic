package io.github.llamarama.team.voidmagic.common.multiblock;

import io.github.llamarama.team.voidmagic.common.block.ChalkBlock;
import io.github.llamarama.team.voidmagic.common.block.util.ChalkType;
import io.github.llamarama.team.voidmagic.common.multiblock.impl.MultiblockType;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;

public final class ModMultiblocks {

    public static final MultiblockType<?> FANCY = MultiblockType.Builder
            .create(IdBuilder.mod("fancy"), 7, 1, 7, true)
            .define('A', DefaultPredicates.any())
            .define('P', DefaultPredicates.match(ModBlocks.OFFERING_PLATE.get()))
            .define('C',
                    DefaultPredicates.match(ModBlocks.CHALK.get().getDefaultState()
                            .with(ChalkBlock.TYPE, ChalkType.AIR)))
            .define('&', DefaultPredicates.match(ModBlocks.WITHERED_STONE_PILLAR.get()))
            .pattern(new String[][]{
                    {
                            "&PCCCP&",
                            "PC   CP",
                            "C     C",
                            "P  A  P",
                            "C     C",
                            "PC   CP",
                            "&PCCCP&"
                    }
            })
            .build();

}
