package io.github.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

public interface BlockPredicate extends BiPredicate<World, BlockPos> {

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
