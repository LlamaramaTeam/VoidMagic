package io.github.llamarama.team.voidmagic.api.multiblock;

import com.google.common.collect.Maps;
import com.ibm.icu.impl.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class MultiblockStructure<T extends TileEntity> {

    private final Vector3i size;
    private final T boundBlock;
    private final HashMap<BlockPos, BlockState> coordinateMap;
    private final HashMap<Character, BlockState> keys;
    private final Pair<BlockPos, TileEntity> origin;
    private int currentX;
    private int currentY;
    private int currentZ;

    /**
     * A multiblock's size should always not be divisible by two.
     * The suze should always be the same as the length of the pattern provided.
     *
     * @param size       The size of the mutliblock.
     * @param boundBlock The tile entity the multiblock is centered on.
     * @param pattern    A 3-dimensional string array containing the pattern the multiblock follows.
     * @param keys       The explanations to all the keys used in the pattern.
     */
    @NotNull
    protected MultiblockStructure(Vector3i size, T boundBlock, String[][] pattern, HashMap<Character, BlockState> keys) {

        if (size.getX() % 2 == 0 || size.getY() % 2 == 0 || size.getZ() % 2 == 0) {
            throw new IllegalStateException("A multiblock can't have dimensions divisible by two.");
        }

        this.size = size;
        this.boundBlock = boundBlock;
        this.keys = keys;
        this.coordinateMap = Maps.newHashMap();

        this.origin = Pair.of(boundBlock.getPos(), boundBlock);
    }

    public void onTickBoundTile() {

    }

    public void onConstructed() {

    }

    public boolean isConstructed() {
        this.onConstructed();
        return this.isValid();
    }

    /**
     * TODO: Finish the check.
     * TODO: Find a way to use the offest provided by the keys map, to determine whether the block at the offest that
     * TODO: is being tested at the moment is the correct one.
     * Do NOT call this method or this class.
     * It is not finished.
     *
     * @return whether the multiblock is constructed.
     */
    public boolean isValid() {
        int sizeX = this.size.getX();
        int sizeY = this.size.getY();
        int sizeZ = this.size.getZ();

        BlockPos originPos = new BlockPos(this.origin.first.getX(), this.origin.first.getY(), this.origin.first.getZ());
        World world = this.origin.second.getWorld();

        for (int j = sizeY + sizeY / 2; j < sizeY - sizeY / 2; j--) {
            for (int i = sizeX + sizeX / 2; i < sizeX - sizeX / 2; i--) {
                for (int k = sizeZ = sizeZ / 2; k < sizeZ - sizeZ / 2; k--) {
                    Vector3i currentOffset = new Vector3i(i, j, k);

                    BlockPos.Mutable mutable = originPos.toMutable().add(i, j, k).toMutable();

                    System.out.println("not quite poggers");
                }
            }
        }
        return false;
    }

    /**
     * Make this a thing
     */
    public static class Builder {


        public MultiblockStructure<?> build() {
            return null;
        }

    }

}
