package io.github.llamarama.team.voidmagic.common.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.Map;

public class MultiblockType<T extends IMultiblock> implements IMultiblockType<T> {

    private final String[] pattern;
    private final Map<Character, BlockState> keys;
    private final Vector3i size;

    public MultiblockType(String[] pattern, Map<Character, BlockState> keys, Vector3i size) {
        this.pattern = pattern;
        this.keys = keys;
        this.size = size;
    }

    @Override
    public boolean existsAt(BlockPos pos, World world) {
        return false;
    }

    @Override
    public T create(BlockPos pos, World world) {
        return null;
    }

}
