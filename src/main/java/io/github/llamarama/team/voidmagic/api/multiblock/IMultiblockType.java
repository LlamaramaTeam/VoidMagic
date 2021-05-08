package io.github.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.Map;

public interface IMultiblockType<MLTB extends IMultiblock> {

    boolean existsAt(BlockPos pos, World world);

    MLTB create(BlockPos pos, World world);

    Map<BlockPos, BlockPredicate> getKeys();

    Vector3i getSize();

    void toTag(CompoundNBT tag);

}
