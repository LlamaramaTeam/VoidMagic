package io.github.llamarama.team.voidmagic.common.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiblockType<T extends IMultiblock> implements IMultiblockType<T> {

    public static final Map<ResourceLocation, MultiblockType<?>> REGISTRY = new ConcurrentHashMap<>();

    private final Map<BlockPos, BlockPredicate> keys;
    private final Vector3i size;


    /*{
        {
            "xxxxxxx",
            "xxxxxxx",
            "xxxxxxx",
            "xxxxxxx",
            "xxxxxxx",
            "xxxxxxx",
            "xxxxxxx"
        },
        {
            "       ",
            "       ",
            "       ",
            "   x   ",
            "       ",
            "       ",
            "       "
        }
      }

      'x', DefaultPredicates.matches(Blocks.BRICKS)
     */
    public MultiblockType(Map<BlockPos, BlockPredicate> keys, Vector3i size) {
        this.keys = keys;
        this.size = size;
    }

    @Override
    public boolean existsAt(BlockPos pos, World world) {
        boolean result = true;
        for (BlockPos currentPos : this.keys.keySet()) {
            BlockPredicate currentPredicate = this.keys.get(currentPos);
            if (!currentPredicate.test(world, pos.add(currentPos))) {
                result = false;
                break;
            }
        }

        return result;
    }

    @Override
    public T create(BlockPos pos, World world) {
        return null;
    }

    public static class Builder<MLB extends IMultiblock> {

        private Vector3i size;
        private Map<Character, BlockPredicate> definitions;
        private Map<BlockPos, Character> pattern;

        private Builder() {

        }

        private static <MBL extends IMultiblock> Builder<MBL> create() {
            return new Builder<>();
        }

        public Builder<MLB> withSize(int x, int y, int z) {
            this.size = new Vector3i(x, y, z);
            this.pattern = new ConcurrentHashMap<>(x * y * z);

            return this;
        }

        public Builder<MLB> define(char character, BlockPredicate predicate) {
            this.definitions.put(character, predicate);

            return this;
        }

        public void pattern(String[][] pattern) {
            if (pattern.length != this.size.getX()) {
                String message = "Cannot parse multiblock. The size of the pattern is bigger than the defined one.";
                throw new RuntimeException(message);
            }

            for (int i = 0; i < pattern.length; i++) {
                for (int j = 0; j < pattern[i].length; j++) {
                    String currentString = pattern[i][j];
                    for (int k = 0; k < currentString.length(); k++) {
                        char charAt = currentString.charAt(i);

                        if (!this.definitions.containsKey(charAt)) {
                            throw new RuntimeException("Cannot find current characted in the definition list.");
                        }

                        BlockPos currentPos = new BlockPos(i, j, k);

                        this.pattern.put(currentPos, charAt);
                    }
                }
            }
        }

        public MultiblockType<MLB> build(ResourceLocation id) {
            if (this.pattern == null
                    || this.pattern.size() == 0
                    || this.size == null
                    || this.definitions == null
                    || this.definitions.size() == 0) {
                throw new RuntimeException("Found problems while attempting to build multiblock");
            }

            HashMap<BlockPos, BlockPredicate> decoded = new HashMap<>();
            for (BlockPos pos : this.pattern.keySet()) {
                BlockPredicate predicate = this.definitions.get(this.pattern.get(pos));
                decoded.put(pos, predicate);
            }

            MultiblockType<MLB> out = new MultiblockType<>(decoded, this.size);
            REGISTRY.put(id, out);
            return out;
        }

    }

}
