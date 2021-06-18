package io.github.llamarama.team.voidmagic.common.lib.multiblock.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.TileEntityPredicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CircleMultiblock extends Multiblock {

    public CircleMultiblock(IMultiblockType type, BlockPos center, World world) {
        super(type, center, MultiblockRotation.ZERO);

        this.setRotation(type.findRotationAt(this.center, world).orElse(MultiblockRotation.ZERO));
    }

    public Collection<BlockPos> tilePositions() {
        Map<BlockPos, BlockPredicate> keys = this.type.getKeysFor(this.getRotation());
        return keys.entrySet().stream()
                .filter((entry) -> entry.getValue() instanceof TileEntityPredicate)
                .map(Map.Entry::getKey)
                .map(this.getPos()::add)
                .map(worldOffsetPos -> worldOffsetPos.add(this.type.getOffset()))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    public AxisAlignedBB getBox() {
        Vector3i size = this.type.getSize();
        return AxisAlignedBB.withSizeAtOrigin(size.getX(), size.getY(), size.getZ())
                .offset(this.getPos())
                .offset(new BlockPos(this.type.getOffset()));
    }

}
