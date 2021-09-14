package io.github.llamarama.team.voidmagic.common.register;

import io.github.llamarama.team.voidmagic.api.block.properties.ChalkType;
import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockType;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.DefaultPredicates;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.DefaultMultiblockType;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.minecraft.block.Blocks;

public final class ModMultiblocks {

    public static final MultiblockType RANDOM_TYPE =
            DefaultMultiblockType.Builder.create(IdBuilder.of("random"),
                            7, 2, 7, false)
                    .define('P', DefaultPredicates.match(ModBlocks.WITHERED_STONE_PILLAR))
                    .define('C',
                            DefaultPredicates.match(ModBlocks.CHALK.getDefaultState().with(ModBlockProperties.CHALK_TYPE, ChalkType.AIR)))
                    .define('D', Blocks.DARK_PRISMARINE)
                    .pattern(new String[][]{
                            {
                                    "P CCC P",
                                    " DCCCD ",
                                    "C CCC C",
                                    "C DCD C",
                                    "C CCC C",
                                    " DCCCD ",
                                    "P CCC P"
                            },
                            {
                                    "P     P",
                                    "D     D",
                                    "D  P  D",
                                    "   P   ",
                                    "D  P  D",
                                    "D     D",
                                    "P     P"
                            }
                    }).build();

    private ModMultiblocks() {

    }

}
