package io.github.llamarama.team.voidmagic.api.multiblock;

/**
 * This is probably going to be used so that an {@link Multiblock} can be attached to a tile entity.
 *
 * @author 0xJoeMama
 * @since 2021
 */
public interface MultiblockProvider {

    Multiblock getMultiblock();

}
