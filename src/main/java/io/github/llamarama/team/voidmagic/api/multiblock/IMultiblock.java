package io.github.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiblock {

    void structureTick();

    boolean existsInLocation(BlockPos pos, World world);

}
