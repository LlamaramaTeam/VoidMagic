package io.github.llamarama.team.voidmagic.common.lib.multiblock.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.Multiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockType;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultMultiblock implements Multiblock {

    protected MultiblockType type;
    protected BlockPos center;
    protected MultiblockRotation rotation;
    private final Supplier<Collection<BlockPos>> positions = () ->
            this.getType().getKeys().get(this.getRotation()).stream().map(Pair::getLeft)
                    .map(this.getPos()::add).map(worldOffsetPos -> worldOffsetPos.add(this.getType().getOffset()))
                    .collect(Collectors.toList());

    public DefaultMultiblock(MultiblockType type, BlockPos center, MultiblockRotation rotation) {
        this.type = type;
        this.center = center;
        this.rotation = rotation;
    }

    @Override
    public MultiblockType getType() {
        return this.type;
    }

    @Override
    public BlockPos getPos() {
        return this.center;
    }

    @Override
    public MultiblockRotation getRotation() {
        return this.rotation;
    }

    @Override
    public @NotNull Collection<BlockPos> positions() {
        return this.positions.get();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        NbtCompound multiblockNbt = new NbtCompound();
        NbtCompound posNbt = NbtHelper.fromBlockPos(this.getPos());
        multiblockNbt.put(NBTConstants.BLOCK_POS, posNbt);

        this.rotation.writeNbt(multiblockNbt);
        this.getType().writeNbt(multiblockNbt);
        nbt.put(NBTConstants.MULTIBLOCK, multiblockNbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        NbtCompound multiblockNbt = nbt.getCompound(NBTConstants.MULTIBLOCK);

        NbtCompound posNbt = multiblockNbt.getCompound(NBTConstants.BLOCK_POS);
        this.center = NbtHelper.toBlockPos(posNbt);

        this.rotation = MultiblockRotation.fromNbt(multiblockNbt).orElse(MultiblockRotation.ZERO);
        this.type = DefaultMultiblockType.fromTag(multiblockNbt).orElseThrow();
    }

}
