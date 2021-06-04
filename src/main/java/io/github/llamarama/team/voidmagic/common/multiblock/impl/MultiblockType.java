package io.github.llamarama.team.voidmagic.common.multiblock.impl;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.common.multiblock.DefaultPredicates;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the {@link IMultiblockType} interface.
 *
 * @param <T> The {@link IMultiblock} this class is a wrapper of.
 * @author 0xJoeMama
 * @since 2021
 */
@SuppressWarnings("unused")
public class MultiblockType<T extends IMultiblock> implements IMultiblockType<T> {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<MultiblockType<?>, ResourceLocation> REGISTRY = new ConcurrentHashMap<>();

    private final Map<BlockPos, BlockPredicate> keys;
    private final Vector3i size;
    private final Vector3i offset;

    public MultiblockType(Map<BlockPos, BlockPredicate> keys, Vector3i size, Vector3i offset) {
        this.keys = keys;
        this.size = size;
        this.offset = offset;
    }

    /**
     * <i><b>There currently is no reason to use this outside of this class but I will leave it public.</b></i>
     * Registers the provided {@link MultiblockType}. I may consider switching to storing the interface for
     * abstraction but for now its the implementation.
     * We register using {@link ResourceLocation} so that it is more organized as well as serializable.
     *
     * @param id   The {@link ResourceLocation} to register this {@link MultiblockType} with.
     * @param type The type to be registered.
     */
    public static <T extends IMultiblock> MultiblockType<T> register(ResourceLocation id, MultiblockType<T> type) {
        // We don't want to have duplicate types registered so we are using putIfAbsent.
        REGISTRY.putIfAbsent(type, id);
        return type;
    }

    /**
     * Deserializes a {@link CompoundNBT} tag to an {@link IMultiblockType}.
     *
     * @param tag The tag that contains the {@link ResourceLocation} that points to this type.
     * @return It is an {@link Optional}. Make sure you throw an {@link RuntimeException} if !{@link Optional#isPresent}.
     */
    public static Optional<IMultiblockType<?>> fromTag(CompoundNBT tag) {
        StringNBT typeIdNBT = (StringNBT) tag.get(NBTConstants.MULTIBLOCK_TYPE_ID);

        if (typeIdNBT != null) {
            MultiblockType<?> out = null;
            ResourceLocation typeId = new ResourceLocation(typeIdNBT.getString());
            for (MultiblockType<?> type : REGISTRY.keySet()) {
                ResourceLocation currentId = REGISTRY.get(type);
                if (currentId.equals(typeId)) {
                    out = type;
                    break;
                }
            }

            return Optional.ofNullable(out);
        }

        // If the NBT is never written we just throw an exception as said above.
        return Optional.empty();
    }

    @Override
    public T create(BlockPos pos, World world) {
        return null;
    }

    @Override
    public boolean existsAt(BlockPos center, World world) {
        boolean result = false;
        BlockPos posAppliedOffset = center.add(this.offset);
        if (!world.isRemote()) {
            result = true;
            for (BlockPos currentPos : this.keys.keySet()) {
                BlockPredicate currentPredicate = this.keys.get(currentPos);
                BlockPos actualPos = posAppliedOffset.add(currentPos);
                if (!currentPredicate.test(world, actualPos)) {
                    VoidMagic.getLogger().debug(
                            String.format("Block at %s is not the expected state!", actualPos)
                    );
                    result = false;
                    break;
                }
                VoidMagic.getLogger().debug("Successfully found block at " + actualPos);
            }
        }

        return result;
    }

    @Override
    public void toTag(CompoundNBT tag) {
        tag.putString(NBTConstants.MULTIBLOCK_TYPE_ID, REGISTRY.get(this).toString());
    }

    @Override
    public Vector3i getSize() {
        return this.size;
    }

    @Override
    public Map<BlockPos, BlockPredicate> getKeys() {
        return this.keys;
    }

    /**
     * Following vanilla traditions we use a {@link Builder} class for <i>Type</i> classes.
     *
     * @param <MLB> The {@link IMultiblock} to wrap.
     * @author 0xJoeMama
     * @since 2021
     */
    public static class Builder<MLB extends IMultiblock> {

        /**
         * The definitions for every specified character.
         */
        private final Map<Character, BlockPredicate> definitions;
        /**
         * The id the structure will be registered with.
         */
        private final ResourceLocation id;
        /**
         * The size of the multiblock.
         */
        private final Vector3i size;
        /**
         * Defines a character for each position in the multiblock. {@link BlockPos} is always an offset from (0, 0, 0).
         */
        private final Map<BlockPos, Character> pattern;
        /**
         * Defines how spaces are handled.
         */
        private final boolean isAbsolute;
        /**
         * The offset from the +x, +y + z corner of the multiblock. By default the multiblock is centered.
         * Override using the {@link Builder#withPlacementOffset} method.
         */
        private Vector3i offset;

        /**
         * A shid load of parameters but let me explain.
         *
         * @param id         The {@link ResourceLocation} the {@link MultiblockType} with be registered with.
         * @param x          The x size.
         * @param y          The y size.
         * @param z          The z size.
         * @param isAbsolute Whether air blocks can cause the {@link MultiblockType} to be disqualified.
         */
        private Builder(ResourceLocation id, int x, int y, int z, boolean isAbsolute) {
            this.isAbsolute = isAbsolute;
            this.definitions = new ConcurrentHashMap<>();
            this.id = id;

            this.size = new Vector3i(x, y, z);
            this.pattern = new HashMap<>(x * y * z);
            this.offset = new Vector3i(-x / 2, -y / 2, -z / 2);
        }

        /**
         * Just create because builder fashions.
         *
         * @return A new builder instance.
         * @see Builder#Builder
         */
        public static <MBL extends IMultiblock> Builder<MBL> create(ResourceLocation id, int x, int y, int z, boolean isAbsolute) {
            return new Builder<>(id, x, y, z, isAbsolute);
        }

        /**
         * Define the predicate for a character.
         *
         * @param character The character.
         * @param predicate The predicate.
         * @return This instance.
         */
        public Builder<MLB> define(char character, BlockPredicate predicate) {
            this.definitions.put(character, predicate);

            return this;
        }

        /**
         * @see Builder#define(char, BlockPredicate)
         */
        public Builder<MLB> define(char character, Block block) {
            return this.define(character, DefaultPredicates.match(block));
        }

        /**
         * Change the offset that the structure is placed in. By default the structure takes in a blockpos for its
         * center. Changing this will make that different.
         * Offset (0, 0, 0) will mean the +x +y +z corner of the multiblock.
         *
         * @return This instance.
         */
        public Builder<MLB> withPlacementOffset(int x, int y, int z) {
            this.offset = new Vector3i(x, y, z);

            return this;
        }

        /**
         * Define the pattern for this multiblock type. This has to be a 2-dimensional String array.
         * You need to have arrays as Y layers, and {@link String}s as X and Z coordinates.
         * Spaces are not considered is the type !{@link Builder#isAbsolute}, however if the type is absolute  spaces
         * are interpreted as air blocks only.
         *
         * @param pattern The pattern.
         * @return This instance.
         */
        public Builder<MLB> pattern(String[][] pattern) {
            // Check if the number of arrays corresponds to the Y size of the multiblock.
            if (pattern.length != this.size.getY()) {
                String message = "Cannot parse multiblock. The size of the pattern is bigger than defined.";
                throw new RuntimeException(message);
            }

            // Iterating.
            for (int i = 0; i < pattern.length; i++) {

                // Check for the Z size using the length of each individual array.
                if (pattern[i].length != this.size.getZ())
                    throw new IllegalStateException("Wrong size for multiblock type " + this);

                // Iterating.
                for (int j = 0; j < pattern[i].length; j++) {

                    // Get the current string to iterate with it.
                    String currentString = pattern[i][j];

                    // Iterating.
                    for (int k = 0; k < currentString.length(); k++) {

                        // Make sure the X size is correct.
                        if (currentString.length() != this.size.getX()) {
                            throw new RuntimeException("Cannot parse multiblock");
                        }

                        // Get the character at i position.
                        char charAt = currentString.charAt(k);
                        // Use the isAbsolute field to remove spaces.
                        if (charAt == ' ' && !this.isAbsolute)
                            continue;

                        // Make sure there is a definition for this character.
                        // This means that every character has to be defined before a pattern is specified since we
                        // parse right after a pattern is defined.
                        if (!this.definitions.containsKey(charAt) && charAt != ' ') {
                            throw new RuntimeException("Cannot find current character in the definition list.");
                        }

                        // Just debugging.
                        VoidMagic.getLogger().debug(this.definitions.get(charAt));
                        // Make a position using the loop's position.
                        BlockPos currentPos = new BlockPos(k, i, j);
                        VoidMagic.getLogger().debug(currentPos);
                        // Add the current position to the pattern.
                        this.pattern.put(currentPos, charAt);
                    }
                }
            }

            return this;
        }

        /**
         * Finalizes the {@link Builder}.
         *
         * @return A new {@link MultiblockType}.
         */
        public MultiblockType<MLB> build() {
            // Check if both a pattern and at least 1 definitions are defined.
            if (this.pattern.size() == 0 || this.definitions.size() == 0) {
                throw new RuntimeException("Found problems while attempting to build multiblock");
            }

            HashMap<BlockPos, BlockPredicate> decoded = new HashMap<>();
            // Add handling for spaces and make sure we map the character to the correct predicate.
            for (BlockPos pos : this.pattern.keySet()) {
                char currentKey = this.pattern.get(pos);
                BlockPredicate predicate;

                if (currentKey == ' ' && isAbsolute) {
                    predicate = DefaultPredicates.match(Blocks.AIR);
                } else
                    predicate = this.definitions.get(currentKey);


                decoded.put(pos, predicate);
            }

            // Return a new multiblock type after we register it. This means that you don't have to register the type
            // yourself.
            MultiblockType<MLB> out = new MultiblockType<>(decoded, this.size, this.offset);

            // Register.
            return register(this.id, out);
        }

        @Override
        public String toString() {
            return String.format("MultiblockType{%s, with size %s", this.id, this.size);
        }

    }

}
