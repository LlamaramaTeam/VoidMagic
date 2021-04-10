package io.llamarama.team.voidmagic.common.tile;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OfferingPlateTileEntity extends TileEntity {

    private final IItemHandler items = new ItemStackHandler(1);
    public LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> items);

    public OfferingPlateTileEntity() {
        super(ModTileEntityTypes.OFFERING_PLATE.get());
        VoidMagic.getLogger().info("Created a new tile entity at " + this.getPos());
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemOptional.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        itemOptional.invalidate();
    }

}
