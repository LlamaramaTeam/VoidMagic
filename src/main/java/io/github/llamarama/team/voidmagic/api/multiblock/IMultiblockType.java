package io.github.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Represents all the {@link IMultiblock}s so that they can be registered.
 * Allows for checking for not bound {@link IMultiblock}s, meaning that you can check whether a multiblock exists at
 * any position.
 * This class also contains the {@link IMultiblockType#getKeys} which is the pattern representing the
 * {@link IMultiblock}.
 *
 * @param <MLTB> The {@link IMultiblock} this class represents.
 * @author 0xJoeMama
 * @since 2021
 */
public interface IMultiblockType<MLTB extends IMultiblock> {

    /**
     * Used to check whether this {@link IMultiblockType} exists at the specified position.
     *
     * @param pos   The position.
     * @param world The world to check in.
     * @return Whether the {@link IMultiblock} is fully formed at the specified position.
     */
    boolean existsAt(BlockPos pos, World world);

    /**
     * TODO: May or may not be implemented,
     */
    MLTB create(BlockPos pos, World world);

    /**
     * Returnes the pattern of the {@link IMultiblock}..
     *
     * @return The pattern.
     */
    Map<BlockPos, BlockPredicate> getKeys();

    /**
     * @return The size of the {@link IMultiblock}.
     */
    Vector3i getSize();

    /**
     * Used fpr serialization of this type.
     *
     * @param tag The tag to serialize with.
     */
    void toTag(CompoundNBT tag);

}
