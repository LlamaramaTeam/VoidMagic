package io.github.llamarama.team.voidmagic.common.register;

import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import io.github.llamarama.team.voidmagic.common.lib.spellbinding.CircleRegistry;
import io.github.llamarama.team.voidmagic.common.lib.spellbinding.impl.SpellbindingCircle;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;

@SuppressWarnings("unused")
public final class ModSpellbindingCircles {

    public static final ISpellbindingCircle A_CIRCLE = CircleRegistry.register(
            () -> new SpellbindingCircle(ModMultiblocks.FANCY,
                    (world, pos, state, circleMultiblock) ->
                            circleMultiblock.positions().forEach(
                                    (currentPos) -> world.removeBlock(currentPos, false))
            ),
            IdBuilder.mod("a_circle"));


    public static void init() {
    }

}
