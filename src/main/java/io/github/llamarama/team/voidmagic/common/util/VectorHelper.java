package io.github.llamarama.team.voidmagic.common.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

public final class VectorHelper {

    public static Vector3i getIntegerVector(int x, int y, int z) {
        return new Vector3i(x, y, z);
    }

    public static Vector3d of(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }

    public static BlockPos multiplyPos(BlockPos pos, int xMult, int yMult, int zMult) {
        return new BlockPos(pos.getX() * xMult, pos.getY() * yMult, pos.getZ() * zMult);
    }

    public static BlockPos multiplyPos(BlockPos pos, Vector3i mult) {
        return multiplyPos(pos, mult.getX(), mult.getY(), mult.getZ());
    }

    public static BlockPos invert(BlockPos initial) {
        return new BlockPos(initial.getZ(), initial.getY(), initial.getX());
    }

}
