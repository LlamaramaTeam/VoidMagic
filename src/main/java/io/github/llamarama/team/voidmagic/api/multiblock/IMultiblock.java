package io.github.llamarama.team.voidmagic.api.multiblock;

import io.github.llamarama.team.voidmagic.common.multiblock.impl.MultiblockType;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Collectors;

public interface IMultiblock {

    boolean existsInLocation(BlockPos pos, World world);

    MultiblockType<?> getType();

    void setType(MultiblockType<?> type);

    default boolean existsAt(BlockPos pos, World world) {
        return this.getType().existsAt(pos, world);
    }

    BlockPos getPos();

    void setPos(BlockPos pos);

    @NotNull
    default Iterable<BlockPos> positions() {
        return this.getType().getKeys().keySet().stream()
                .map(this.getPos()::add)
                .collect(Collectors.toSet());
    }

    default CompoundNBT serialize(CompoundNBT tag) {
        CompoundNBT posTag = NBTUtil.writeBlockPos(this.getPos());
        tag.put(NBTConstants.BLOCK_POS, posTag);

        this.getType().toTag(tag);
        return tag;
    }

    default void deserialize(CompoundNBT tag) {
        this.setPos(NBTUtil.readBlockPos(tag.getCompound(NBTConstants.BLOCK_POS)));

        Optional<MultiblockType<?>> type = MultiblockType.fromTag(tag);
        type.ifPresent(this::setType);
        if (!type.isPresent())
            throw new RuntimeException("Cannot find the target multiblock type from tag: " + tag);
    }

}
