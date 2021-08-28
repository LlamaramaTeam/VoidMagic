package io.github.llamarama.team.voidmagic.common.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

public final class VectorHelper {

    @NotNull
    public static Vec3i getIntegerVector(int x, int y, int z) {
        return new Vec3i(x, y, z);
    }

    @NotNull
    public static Vec3d of(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }

    @NotNull
    public static BlockPos multiplyPos(BlockPos pos, int xMult, int yMult, int zMult) {
        return new BlockPos(pos.getX() * xMult, pos.getY() * yMult, pos.getZ() * zMult);
    }

    @NotNull
    public static BlockPos multiply(BlockPos pos, Vec3i mult) {
        return multiplyPos(pos, mult.getX(), mult.getY(), mult.getZ());
    }

    public static BlockPos invertHorizontalAxis(BlockPos firstPos) {
        return new BlockPos(firstPos.getZ(), firstPos.getY(), firstPos.getX());
    }

}
