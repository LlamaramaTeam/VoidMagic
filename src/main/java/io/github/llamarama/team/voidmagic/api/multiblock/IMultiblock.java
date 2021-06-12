package io.github.llamarama.team.voidmagic.api.multiblock;

import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.MultiblockType;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IMultiblock {

    IMultiblockType getType();

    void setType(IMultiblockType type);

    BlockPos getPos();

    void setPos(BlockPos pos);

    default boolean exists(World world) {
        return this.getType().existsAt(this.getPos(), world);
    }

    @NotNull
    default Collection<BlockPos> positions() {
        // Joe fixed this lol.
        return this.getType().getKeys().get(MultiblockRotation.ZERO)
                .stream()
                .map(Pair::getKey)
                .map(this.getPos()::add)
                .map((worldOffsetPos) -> worldOffsetPos.add(this.getType().getOffset()))
                .collect(Collectors.toSet());
    }

    default void serialize(CompoundNBT tag) {
        CompoundNBT multiblockTag = new CompoundNBT();
        CompoundNBT posTag = NBTUtil.writeBlockPos(this.getPos());
        multiblockTag.put(NBTConstants.BLOCK_POS, posTag);

        this.getType().toTag(multiblockTag);
        tag.put(NBTConstants.MULTIBLOCK_SERIAL_TAG, multiblockTag);
    }

    default void deserialize(CompoundNBT tag) {
        CompoundNBT multiblockTag = tag.getCompound(NBTConstants.MULTIBLOCK_SERIAL_TAG);
        this.setPos(NBTUtil.readBlockPos(multiblockTag.getCompound(NBTConstants.BLOCK_POS)));

        Optional<IMultiblockType> type = MultiblockType.fromTag(multiblockTag);
        if (!type.isPresent())
            throw new RuntimeException("Cannot find the target multiblock type from tag: " + tag);
        else
            type.ifPresent(this::setType);
    }

}
