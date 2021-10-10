package io.github.llamarama.team.voidmagic.common.lib.multiblock.impl;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockType;
import io.github.llamarama.team.voidmagic.api.multiblock.PositionPredicate;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.DefaultPredicates;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;

/**
 * Default implementation of the {@link MultiblockType} interface.
 *
 * @author 0xJoeMama
 * @since 2021
 */
public class DefaultMultiblockType implements MultiblockType {

    public static final Map<MultiblockType, Identifier> REGISTRY = new HashMap<>();

    private final SetMultimap<MultiblockRotation, Pair<BlockPos, PositionPredicate>> keys;
    private final Vec3i size;
    private final Vec3i offset;

    private DefaultMultiblockType(Map<BlockPos, PositionPredicate> decoded, Vec3i size, Vec3i offset) {
        this.keys = Multimaps.newSetMultimap(new EnumMap<>(MultiblockRotation.class), HashSet::new);
        this.size = size;
        this.offset = offset;

        this.initializeWithRotations(decoded);
    }

    /**
     * Deserializes a {@link NbtCompound} tag to a {@link MultiblockType}.
     *
     * @param tag The tag that contains the {@link Identifier} that points to this type.
     * @return It is an {@link Optional}. Make sure you throw an {@link RuntimeException} if !{@link Optional#isPresent}.
     */
    public static Optional<MultiblockType> fromTag(NbtCompound tag) {
        NbtString typeIdNBT = (NbtString) tag.get(NBTConstants.MULTIBLOCK_TYPE_ID);

        if (typeIdNBT != null) {
            MultiblockType out = null;
            Identifier typeId = new Identifier(typeIdNBT.asString());
            for (MultiblockType type : REGISTRY.keySet()) {
                Identifier currentId = REGISTRY.get(type);
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

    private static MultiblockType register(Identifier id, MultiblockType out) {
        REGISTRY.computeIfPresent(out, (type, identifier) -> {
            throw new RuntimeException("Attempted to register multiblock type: " + type + " more than once!\n" +
                    "Ids : " + id + ", " + REGISTRY.get(out));
        });

        REGISTRY.put(out, id);

        return out;
    }

    @Override
    public boolean existsAt(BlockPos center, World world) {
        boolean exists = false;

        for (MultiblockRotation rot : MultiblockRotation.values()) {
            exists = this.validateRotationAt(center, world, rot);

            if (exists) {
                break;
            }
        }

        return exists;
    }

    @Override
    public SetMultimap<MultiblockRotation, Pair<BlockPos, PositionPredicate>> getKeys() {
        return this.keys;
    }

    @Override
    public Optional<MultiblockRotation> findRotationAt(BlockPos center, World world) {
        boolean result;

        for (MultiblockRotation rot : MultiblockRotation.values()) {
            result = this.validateRotationAt(center, world, rot);

            if (result) {
                return Optional.of(rot);
            }
        }

        return Optional.empty();
    }

    @Override
    public Vec3i getSize() {
        return this.size;
    }

    @Override
    public Vec3i getOffset() {
        return this.offset;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putString(NBTConstants.MULTIBLOCK_TYPE_ID, REGISTRY.get(this).toString());
    }

    private boolean validateRotationAt(BlockPos center, World world, MultiblockRotation rot) {
        BlockPos actualPos = center.add(rot.transform(new BlockPos(this.offset)));

        for (Pair<BlockPos, PositionPredicate> pair : this.keys.get(rot)) {
            BlockPos testPos = actualPos.add(pair.getLeft());
            PositionPredicate predicate = pair.getRight();

            if (!predicate.checkPos(world, testPos)) {
                world.getPlayers().forEach(playerEntity -> playerEntity.sendMessage(new LiteralText("Block at " + testPos + " could not be found! Rotation : " + rot), false));
                return false;
            }
        }


        return true;
    }

    private void initializeWithRotations(Map<BlockPos, PositionPredicate> decoded) {
        for (MultiblockRotation rot : MultiblockRotation.values()) {
            decoded.forEach((pos, predicate) ->
                    this.keys.put(rot, new Pair<>(rot.transform(pos), predicate))
            );
        }
    }

    /**
     * Following vanilla traditions we use a {@link Builder} class for <i>Type</i> classes.
     *
     * @author 0xJoeMama
     * @since 2021
     */
    public static class Builder {

        /**
         * Defines how spaces are handled.
         */
        private final boolean isAbsolute;
        /**
         * The definitions for every specified character.
         */
        private final Map<Character, PositionPredicate> definitions;
        /**
         * The id the structure will be registered with.
         */
        private final Identifier id;
        /**
         * The size of the multiblock.
         */
        private final Vec3i size;
        /**
         * Defines a character for each position in the multiblock. {@link BlockPos} is always an offset from (0, 0, 0).
         */
        private final Map<BlockPos, Character> pattern;
        /**
         * The offset from the +x, +y + z corner of the multiblock. By default the multiblock is centered.
         * Override using the {@link Builder#withPlacementOffset} method.
         */
        private Vec3i offset;

        /**
         * A shid load of parameters but let me explain.
         *
         * @param id         The {@link Identifier} the {@link MultiblockType} with be registered with.
         * @param xSize      The x size.
         * @param ySize      The y size.
         * @param zSize      The z size.
         * @param isAbsolute Whether air blocks can cause the {@link MultiblockType} to be disqualified.
         */
        private Builder(Identifier id, int xSize, int ySize, int zSize, boolean isAbsolute) {
            this.isAbsolute = isAbsolute;
            this.definitions = new HashMap<>();
            this.id = id;

            this.size = new Vec3i(xSize, ySize, zSize);
            this.pattern = new HashMap<>(xSize * ySize * zSize);
            this.offset = new Vec3i(-xSize / 2, -ySize / 2, -zSize / 2);
        }

        /**
         * Just create because builder fashions.
         *
         * @return A new builder instance.
         * @see Builder#Builder
         */
        public static Builder create(Identifier id, int x, int y, int z, boolean isAbsolute) {
            return new Builder(id, x, y, z, isAbsolute);
        }

        /**
         * Define the predicate for a character.
         *
         * @param character The character.
         * @param predicate The predicate.
         * @return This instance.
         */
        public Builder define(char character, PositionPredicate predicate) {
            this.definitions.putIfAbsent(character, predicate);

            return this;
        }

        /**
         * @see Builder#define(char, PositionPredicate)
         */
        public Builder define(char character, Block block) {
            return this.define(character, DefaultPredicates.match(block));
        }

        /**
         * Change the offset that the structure is placed in. By default the structure takes in a blockpos for its
         * center. Changing this will make that different.
         * Offset (0, 0, 0) will mean the +x +y +z corner of the multiblock.
         * Also by default the offset of a multiblock is the literal center of the area it covers.
         *
         * @return This instance.
         */
        public Builder withPlacementOffset(int x, int y, int z) {
            this.offset = new Vec3i(x, y, z);

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
        public Builder pattern(String[][] pattern) {
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

                    // Make sure the X size is correct.
                    if (currentString.length() != this.size.getX()) {
                        throw new RuntimeException("Cannot parse multiblock");
                    }

                    // Iterating.
                    for (int k = 0; k < currentString.length(); k++) {

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

                        // Make a position using the loop's position.
                        BlockPos currentPos = new BlockPos(k, i, j);
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
        public MultiblockType build() {
            // Check if both a pattern and at least 1 definitions are defined.
            if (this.pattern.size() == 0 || this.definitions.size() == 0) {
                throw new RuntimeException("Found problems while attempting to build multiblock");
            }

            HashMap<BlockPos, PositionPredicate> decoded = new HashMap<>();
            // Add handling for spaces and make sure we map the character to the correct predicate.
            for (BlockPos pos : this.pattern.keySet()) {
                char currentKey = this.pattern.get(pos);
                PositionPredicate predicate;

                if (currentKey == ' ' && isAbsolute) {
                    predicate = DefaultPredicates.match(Blocks.AIR);
                } else
                    predicate = this.definitions.get(currentKey);


                decoded.put(pos, predicate);
            }

            // Return a new multiblock type after we register it. This means that you don't have to register the type
            // yourself.
            MultiblockType out = new DefaultMultiblockType(decoded, this.size, this.offset);

            // Register.
            return register(this.id, out);
        }

        @Override
        public String toString() {
            return String.format("MultiblockType{%s, with size %s", this.id, this.size);
        }


    }

}
