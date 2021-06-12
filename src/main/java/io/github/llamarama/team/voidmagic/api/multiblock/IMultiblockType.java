package io.github.llamarama.team.voidmagic.api.multiblock;

import com.google.common.collect.SetMultimap;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.MultiblockType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * Represents all the {@link IMultiblock}s so that they can be registered.
 * Allows for checking for not bound {@link IMultiblock}s, meaning that you can check whether a multiblock exists at
 * any position.
 * This class also contains the {@link IMultiblockType#getKeys} which is the pattern representing the
 * {@link IMultiblock}.
 *
 * @author 0xJoeMama
 * @since 2021
 */
public interface IMultiblockType {

    /**
     * Used to check whether this {@link IMultiblockType} exists at the specified position.
     *
     * @param center The position.
     * @param world  The world to check in.
     * @return Whether the {@link IMultiblock} is fully formed at the specified position.
     */
    boolean existsAt(BlockPos center, World world);

    /**
     * Returnes the pattern of the {@link IMultiblock}..
     *
     * @return The pattern.
     */
    SetMultimap<MultiblockRotation, Pair<BlockPos, BlockPredicate>> getKeys();

    Map<BlockPos, BlockPredicate> getKeysFor(MultiblockRotation rotation);

    Map<BlockPos, BlockPredicate> getDefaultKeys();

    /**
     * @return The size of the {@link IMultiblock}.
     */
    Vector3i getSize();

    /**
     * Returns the default offset of the multiblock.
     *
     * @see MultiblockType#getOffset
     */
    Vector3i getOffset();

    /**
     * Used fpr serialization of this type.
     *
     * @param tag The tag to serialize with.
     */
    void toTag(CompoundNBT tag);

}
