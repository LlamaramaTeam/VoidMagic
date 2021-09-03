package io.github.llamarama.team.voidmagic.api.spellbinding;

import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.CircleMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Spellbindable {

    void circleFormed(World world, BlockPos circleCenter, BlockState state, CircleMultiblock circleMultiblock);

}
