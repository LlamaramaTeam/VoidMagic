package io.github.llamarama.team.voidmagic.api.spellbinding;

import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.CircleMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISpellbindable {

    void circleFormed(World world, BlockPos pos, BlockState state, CircleMultiblock circleMultiblock);

}
