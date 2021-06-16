package io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates;

import io.github.llamarama.team.voidmagic.api.multiblock.BlockPredicate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPredicate implements BlockPredicate {

    private final TileEntityType<? extends TileEntity> tileEntityType;

    public TileEntityPredicate(TileEntityType<? extends TileEntity> tileEntityType) {
        this.tileEntityType = tileEntityType;
    }

    @Override
    public boolean test(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tileEntity != null && tileEntity.getType() == this.tileEntityType;
    }

    public TileEntityType<? extends TileEntity> getTileEntityType() {
        return tileEntityType;
    }

}
