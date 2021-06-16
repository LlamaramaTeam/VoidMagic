package io.github.llamarama.team.voidmagic.api.spellbinding;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.CircleMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public interface ISpellbindingCircle {

    Supplier<? extends CircleMultiblock> multiblock(BlockPos center, World world);

    ISpellbindable getResult();

    int getCraftingTime();

    IMultiblockType getMultiblockType();

}
