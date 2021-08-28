package io.github.llamarama.team.voidmagic.api.multiblock;

import com.google.common.collect.SetMultimap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents all the {@link Multiblock}s so that they can be registered.
 * Allows for checking for not bound {@link Multiblock}s, meaning that you can check whether a multiblock exists at
 * any position.
 * This class also contains the {@link MultiblockType#getKeys} which is the pattern representing the
 * {@link Multiblock}.
 *
 * @author 0xJoeMama
 * @since 2021
 */
public interface MultiblockType {

    /**
     * Used to check whether this {@link Multiblock} exists at the specified position.
     *
     * @param center The position.
     * @param world  The world to check in.
     * @return Whether the {@link Multiblock} is fully formed at the specified position.
     */
    boolean existsAt(BlockPos center, World world);

    /**
     * Returnes the pattern of the {@link Multiblock}..
     *
     * @return The pattern.
     */
    SetMultimap<MultiblockRotation, Pair<BlockPos, PositionPredicate>> getKeys();

    default Map<BlockPos, PositionPredicate> getKeysFor(MultiblockRotation rotation) {
        return this.getKeys().get(rotation).stream()
                .collect(HashMap::new, (map, pair) -> map.put(pair.getLeft(), pair.getRight()), HashMap::putAll);
    }

    default Map<BlockPos, PositionPredicate> getDefaultKeys() {
        return this.getKeysFor(MultiblockRotation.ZERO);
    }

    Optional<MultiblockRotation> findRotationAt(BlockPos center, World world);

    /**
     * @return The size of the {@link Multiblock}.
     */
    Vec3i getSize();

    /**
     * Returns the default offset of the multiblock.
     *
     * @see MultiblockType#getOffset
     */
    Vec3i getOffset();

    /**
     * Used fpr serialization of this type.
     *
     * @param nbt The tag to serialize with.
     */
    void writeNbt(NbtCompound nbt);

}
