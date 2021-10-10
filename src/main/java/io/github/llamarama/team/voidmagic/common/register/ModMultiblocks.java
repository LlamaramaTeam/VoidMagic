package io.github.llamarama.team.voidmagic.common.register;

import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockType;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.DefaultPredicates;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.DefaultMultiblockType;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.minecraft.block.Blocks;

public final class ModMultiblocks {

    public static final MultiblockType RANDOM_TYPE =
            DefaultMultiblockType.Builder.create(IdBuilder.of("random"),
                            5, 1, 6, false)
                    .define('P', DefaultPredicates.match(Blocks.WHITE_WOOL))
                    .pattern(new String[][]{
                            {
                                    "PPPPP",
                                    "PPPPP",
                                    "PPPPP",
                                    "PPPPP",
                                    "PPPPP",
                                    "PPPPP"
                            }
                    }).build();

    private ModMultiblocks() {

    }

    public static void init() {

    }

}
