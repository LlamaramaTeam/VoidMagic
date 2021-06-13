package io.github.llamarama.team.voidmagic.common.register;

import io.github.llamarama.team.voidmagic.common.lib.multiblock.DefaultPredicates;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.MultiblockType;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;

public final class ModMultiblocks {

    public static final MultiblockType<?> FANCY = MultiblockType.Builder
            .create(IdBuilder.mod("fancy"), 3, 1, 3, true)
            .define('A', DefaultPredicates.any())
            .define('C', DefaultPredicates.match(ModBlocks.TOFAL.get()))
            .pattern(new String[][]{
                    {
                            "CCC",
                            "CAC",
                            "CCC"
                    }
            })
            .build();

}
