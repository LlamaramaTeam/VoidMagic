package io.github.llamarama.team.voidmagic.common.impl;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.common.multiblock.DefaultPredicates;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiblockType<T extends IMultiblock> implements IMultiblockType<T> {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<ResourceLocation, MultiblockType<?>> REGISTRY = new ConcurrentHashMap<>();

    private final Map<BlockPos, BlockPredicate> keys;
    private final Vector3i size;
    private final Vector3i offset;

    public MultiblockType(Map<BlockPos, BlockPredicate> keys, Vector3i size, Vector3i offset) {
        this.keys = keys;
        this.size = size;
        this.offset = offset;
    }

    public static void register(ResourceLocation id, MultiblockType<?> type) {
        REGISTRY.put(id, type);
    }

    @Override
    public T create(BlockPos pos, World world) {
        return null;
    }

    @Override
    public boolean existsAt(BlockPos pos, World world) {
        boolean result = false;
        if (!world.isRemote()) {
            result = true;
            for (BlockPos currentPos : this.keys.keySet()) {
                BlockPredicate currentPredicate = this.keys.get(currentPos);
                BlockPos actualPos = pos.add(currentPos).add(this.offset);
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

    public static class Builder<MLB extends IMultiblock> {

        private final Map<Character, BlockPredicate> definitions;
        private final ResourceLocation id;
        private Vector3i size;
        private Map<BlockPos, Character> pattern;
        private final boolean isAbsolute;
        private Vector3i offset;

        private Builder(ResourceLocation id, boolean isAbsolute) {
            this.isAbsolute = isAbsolute;
            this.definitions = new ConcurrentHashMap<>();
            this.id = id;
        }

        public static <MBL extends IMultiblock> Builder<MBL> create(ResourceLocation id, boolean isAbsolute) {
            return new Builder<>(id, isAbsolute);
        }

        public Builder<MLB> withSize(int x, int y, int z) {
            this.size = new Vector3i(x, y, z);
            this.pattern = new ConcurrentHashMap<>(x * y * z);
            this.offset = new Vector3i(-x / 2, -y / 2, -z / 2);

            return this;
        }

        public Builder<MLB> define(char character, BlockPredicate predicate) {
            this.definitions.put(character, predicate);

            return this;
        }

        public Builder<MLB> withPlacementOffset(int x, int y, int z) {
            this.offset = new Vector3i(x, y, z);

            return this;
        }

        public Builder<MLB> pattern(String[][] pattern) {
            if (pattern.length != this.size.getY()) {
                String message = "Cannot parse multiblock. The size of the pattern is bigger than defined.";
                throw new RuntimeException(message);
            }

            for (int i = 0; i < pattern.length; i++) {

                if (pattern[i].length != this.size.getZ())
                    throw new IllegalStateException("Wrong size for multiblock type " + this);

                for (int j = 0; j < pattern[i].length; j++) {

                    String currentString = pattern[i][j];

                    for (int k = 0; k < currentString.length(); k++) {

                        if (currentString.length() != this.size.getX()) {
                            throw new RuntimeException("Cannot parse multiblock");
                        }

                        char charAt = currentString.charAt(k);
                        if (charAt == ' ' && !this.isAbsolute)
                            continue;

                        if (!this.definitions.containsKey(charAt) && charAt != ' ') {
                            throw new RuntimeException("Cannot find current character in the definition list.");
                        }

                        VoidMagic.getLogger().debug(this.definitions.get(charAt));
                        BlockPos currentPos = new BlockPos(k, i, j);
                        VoidMagic.getLogger().debug(currentPos);
                        this.pattern.put(currentPos, charAt);
                    }
                }
            }

            return this;
        }

        public MultiblockType<MLB> build() {
            if (this.pattern == null || this.pattern.size() == 0 || this.size == null || this.definitions.size() == 0) {
                throw new RuntimeException("Found problems while attempting to build multiblock");
            }

            HashMap<BlockPos, BlockPredicate> decoded = new HashMap<>();
            for (BlockPos pos : this.pattern.keySet()) {
                char currentKey = this.pattern.get(pos);
                BlockPredicate predicate;

                if (currentKey == ' ' && isAbsolute) {
                    predicate = DefaultPredicates.match(Blocks.AIR);
                } else
                    predicate = this.definitions.get(currentKey);


                decoded.put(pos, predicate);
            }


            MultiblockType<MLB> out = new MultiblockType<>(decoded, this.size, this.offset);
            register(this.id, out);
            return out;
        }

        @Override
        public String toString() {
            return String.format("MultiblockType{ %s, with size %s", this.id, this.size.toString());
        }

    }

}
