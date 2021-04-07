package io.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public interface IMultiblockContainer<T extends TileEntity> extends ITickableTileEntity {

    MultiblockStructure<T> getMultiblock();

    @Override
    default void tick() {
        if (this.getMultiblock().isConstructed()) {
            this.getMultiblock().onTickBoundTile();
        }
    }

}
