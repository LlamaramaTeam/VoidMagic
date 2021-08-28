package io.github.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Represents a position in a multiblock. You can either use a lambda with this, use an implementation of this class
 * or create your own implementation.
 *
 * @author 0xJoeMama
 * @since 2021
 */
@FunctionalInterface
public interface PositionPredicate {

    /**
     * The abstract method.
     *
     * @param world The world to check in.
     * @param pos   The position to check ih,
     * @return Whether the position can be accepted by the target multiblock.
     */
    boolean checkPos(World world, BlockPos pos);

}
