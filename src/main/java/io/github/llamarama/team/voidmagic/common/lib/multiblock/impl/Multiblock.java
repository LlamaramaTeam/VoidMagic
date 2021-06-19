package io.github.llamarama.team.voidmagic.common.lib.multiblock.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Lazy;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

public class Multiblock implements IMultiblock {

    protected IMultiblockType type;
    protected BlockPos center;
    private MultiblockRotation rotation;
    private final Lazy<Collection<BlockPos>> positions = Lazy.of(() ->
            this.getType().getKeys().get(this.getRotation())
                    .stream()
                    .map(Pair::getKey)
                    .map(this.getPos()::add)
                    .map((worldOffsetPos) -> worldOffsetPos.add(this.getType().getOffset()))
                    .collect(Collectors.toSet()));

    public Multiblock(IMultiblockType type, BlockPos center, MultiblockRotation rotation) {
        this.type = type;
        this.center = center;
        this.rotation = rotation;
    }

    @Override
    public IMultiblockType getType() {
        return this.type;
    }

    @Override
    public void setType(IMultiblockType type) {
        this.type = type;
    }

    @Override
    public BlockPos getPos() {
        return this.center;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.center = pos;
    }

    @Override
    public MultiblockRotation getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(MultiblockRotation rotation) {
        this.rotation = rotation;
    }

    @Override
    public @NotNull Collection<BlockPos> positions() {
        return this.positions.get();
    }

}
