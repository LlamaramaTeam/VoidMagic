package io.github.llamarama.team.voidmagic.common.tile;

import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockProvider;
import io.github.llamarama.team.voidmagic.common.multiblock.ModMultiblocks;
import io.github.llamarama.team.voidmagic.common.multiblock.impl.Multiblock;
import io.github.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ScrollTileEntity extends TileEntity implements ITickableTileEntity, IMultiblockProvider {

    private final IMultiblock multiblock;

    public ScrollTileEntity() {
        super(ModTileEntityTypes.SCROLL.get());
        this.multiblock = new Multiblock(ModMultiblocks.FANCY, this.getPos());
    }

    @Override
    public void tick() {
        if (this.world == null)
            return;

        if (!this.world.isRemote()) {
            // Server logic

            // Check for correct position.
            BlockState currentState = this.getBlockState();
            if (currentState.get(ModBlockProperties.OPEN) && !currentState.isValidPosition(this.world, this.getPos()))
                this.world.destroyBlock(this.getPos(), true);

//            VoidMagic.getLogger().debug(this.multiblock.getPos());
            // Start the circle logic.
            if (this.multiblock.exists(this.world)) {
                this.multiblock.positions().forEach(pos -> this.world.removeBlock(pos, false));
            }
        }

        // Maybe some client logic
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.multiblock.deserialize(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT tag = super.write(compound);
        this.multiblock.serialize(tag);
        this.multiblock.setPos(this.getPos());
        return tag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        Optional.ofNullable(this.world).ifPresent((world) -> {
            CompoundNBT tag = pkt.getNbtCompound();
            BlockPos pos = pkt.getPos();

            this.handleUpdateTag(world.getBlockState(pos), tag);
        });
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
    }

    @Override
    public IMultiblock getMultiblock() {
        return this.multiblock;
    }

}
