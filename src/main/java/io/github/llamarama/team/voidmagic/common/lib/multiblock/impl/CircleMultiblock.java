package io.github.llamarama.team.voidmagic.common.lib.multiblock.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindable;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.TileEntityPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CircleMultiblock extends Multiblock implements ISpellbindable {

    public CircleMultiblock(IMultiblockType type, BlockPos center, World world) {
        super(type, center, MultiblockRotation.ZERO);

        this.setRotation(type.findRotationAt(this.center, world).orElse(MultiblockRotation.ZERO));
    }

    public List<BlockPos> tilePositions() {
        List<BlockPos> out = new ArrayList<>(this.type.getDefaultKeys().size());

        this.type.getKeysFor(this.getRotation()).forEach((pos, predicate) -> {
            if (predicate instanceof TileEntityPredicate)
                out.add(pos);
        });

        return out.stream()
                .map(this.getPos()::add)
                .map(worldOffsetPos -> worldOffsetPos.add(this.type.getOffset()))
                .collect(Collectors.toList());
    }

    public AxisAlignedBB getBox() {
        Vector3i size = this.type.getSize();
        return AxisAlignedBB.withSizeAtOrigin(size.getX(), size.getY(), size.getZ())
                .offset(this.getPos())
                .offset(new BlockPos(this.type.getOffset()));
    }

    @Override
    public void circleFormed(World world, BlockPos pos, BlockState state, CircleMultiblock circleMultiblock) {

    }

}
