package io.github.llamarama.team.voidmagic.common.lib.multiblock.impl;

import io.github.llamarama.team.voidmagic.api.multiblock.BoxedMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiBlockEntitySystem;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockType;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockEntityPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class CircleMultiblock extends DefaultMultiblock implements MultiBlockEntitySystem, BoxedMultiblock {


    public CircleMultiblock(MultiblockType type, BlockPos center, World world) {
        super(type, center, MultiblockRotation.ZERO);

        this.rotation = this.type.findRotationAt(center, world).orElse(MultiblockRotation.ZERO);
    }

    @Override
    public Collection<BlockPos> getBlockEntityPositions() {
        return this.getType().getKeysFor(this.getRotation()).entrySet().stream()
                .filter((entry) -> entry.getValue() instanceof BlockEntityPredicate)
                .map(Map.Entry::getKey)
                .map(this.getPos()::add)
                .map((worldOffsetPos) -> worldOffsetPos.add(this.getType().getOffset()))
                .collect(Collectors.toList());
    }

    @Override
    public Box getBox() {
        Vec3i size = this.getType().getSize();
        return Box.of(Vec3d.ofCenter(this.getPos()), size.getX(), size.getY(), size.getZ())
                .offset(Vec3d.ofCenter(this.getType().getOffset()));
    }

}
