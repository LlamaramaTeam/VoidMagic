package io.github.llamarama.team.voidmagic.api.multiblock;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class MultiblockType<T extends TileEntity> {

    private final HashMap<Character, BlockState> keys;
    private final HashMap<BlockPos, BlockState> structureMap;

    private MultiblockType(String[][] pattern, HashMap<Character, BlockState> keys) {
        this.keys = keys;
        structureMap = Maps.newHashMap();

        this.decodeToMultiblock(pattern);
    }

    private void decodeToMultiblock(String[][] pattern) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                for (int k = 0; k < pattern[i][j].length(); k++) {
                    char current = pattern[i][j].charAt(k);
                    if (this.keys.containsKey(current)) {
                        this.structureMap.put(mutable.setPos(i, j, k), this.keys.get(current));
                    }
                }
            }
        }
    }

    public MultiblockStructure<T> create() {
        return null;
    }


    public static class Builder {

    }

}
