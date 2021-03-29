package io.llamarama.team.voidmagic.api.multiblock;

import net.minecraft.tileentity.ITickableTileEntity;

public interface IMultiblockContainer extends ITickableTileEntity {

    MultiblockStructure getMultiblock();

    @Override
    default void tick() {
        if (this.getMultiblock().isConstructed()) {
            this.getMultiblock().onTickBoundTile();
        }
    }

}
