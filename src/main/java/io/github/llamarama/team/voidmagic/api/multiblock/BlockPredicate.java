package io.github.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

/**
 * Represents a position in a multiblock. You can either use a lambda with this, use an implementation of this class
 * or create your own implementation.
 *
 * @author 0xJoeMama
 * @since 2021
 */
public interface BlockPredicate extends BiPredicate<World, BlockPos> {

    /**
     * The abstract method.
     *
     * @param world    The world to check in.
     * @param blockPos The position to check ih,
     * @return Whether that position is in the correct state.
     */
    @Override
    boolean test(World world, BlockPos blockPos);

    @NotNull
    @Override
    default BiPredicate<World, BlockPos> and(@NotNull BiPredicate<? super World, ? super BlockPos> other) {
        return BiPredicate.super.and(other);
    }

    @NotNull
    @Override
    default BiPredicate<World, BlockPos> negate() {
        return BiPredicate.super.negate();
    }

    @NotNull
    @Override
    default BiPredicate<World, BlockPos> or(@NotNull BiPredicate<? super World, ? super BlockPos> other) {
        return BiPredicate.super.or(other);
    }

}
