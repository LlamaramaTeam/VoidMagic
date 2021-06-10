package io.github.llamarama.team.voidmagic.api.multiblock;

import io.github.llamarama.team.voidmagic.common.util.VectorHelper;
import net.minecraft.util.math.BlockPos;

public enum MultiblockRotation {

    ZERO(1, 1),
    NINETY(-1, 1),
    ONE_EIGHTY(-1, -1),
    TWO_SEVENTY(1, -1);

    private final int xMult;
    private final int zMult;

    MultiblockRotation(int xMult, int zMult) {
        this.xMult = xMult;
        this.zMult = zMult;
    }

    public BlockPos transform(BlockPos initial) {
        switch (this) {
            case ZERO:
            case ONE_EIGHTY:
                return VectorHelper.multiplyPos(initial, this.xMult, 1, this.zMult);
            case NINETY:
            case TWO_SEVENTY:
                return VectorHelper.multiplyPos(VectorHelper.invert(initial), this.xMult, 1, this.zMult);
            default:
                return ZERO.transform(initial);
        }
    }

}
