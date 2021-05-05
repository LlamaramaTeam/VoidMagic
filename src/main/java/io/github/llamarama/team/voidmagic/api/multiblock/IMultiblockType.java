package io.github.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiblockType<MLTB extends IMultiblock> {

    boolean existsAt(BlockPos pos, World world);

    MLTB create(BlockPos pos, World world);

}
