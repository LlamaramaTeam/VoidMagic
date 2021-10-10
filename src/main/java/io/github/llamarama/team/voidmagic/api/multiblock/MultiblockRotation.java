package io.github.llamarama.team.voidmagic.api.multiblock;

import io.github.llamarama.team.voidmagic.common.util.VectorHelper;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Optional;

public enum MultiblockRotation {

    ZERO((byte) 1, (byte) 1),
    NINETY((byte) -1, (byte) 1),
    ONE_EIGHTY((byte) -1, (byte) -1),
    TWO_SEVENTY((byte) 1, (byte) -1);

    private final byte xMult;
    private final byte zMult;

    MultiblockRotation(byte xMult, byte zMult) {
        this.xMult = xMult;
        this.zMult = zMult;
    }

    public static Optional<MultiblockRotation> fromNbt(NbtCompound nbt) {
        int ordinal = nbt.getInt(NBTConstants.MULTIBLOCK_ROTATION);

        return Arrays.stream(values())
                .filter(multiblockRotation -> multiblockRotation.ordinal() == ordinal)
                .findFirst();
    }

    public BlockPos transform(BlockPos pos) {
        return switch (this) {
            case ZERO, ONE_EIGHTY -> VectorHelper.multiplyPos(pos, this.xMult, 1, this.zMult);
            case NINETY, TWO_SEVENTY -> VectorHelper.multiplyPos(VectorHelper.invertHorizontalAxis(pos), this.xMult, 1, this.zMult);
        };
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putInt(NBTConstants.MULTIBLOCK_ROTATION, this.ordinal());
    }

}
