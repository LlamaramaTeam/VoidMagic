package io.llamarama.team.voidmagic.util;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

public final class VectorHelper {

    public static Vector3i getIntegerVector(int x, int y, int z) {
        return new Vector3i(x, y, z);
    }

    public static Vector3d of(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }

}
