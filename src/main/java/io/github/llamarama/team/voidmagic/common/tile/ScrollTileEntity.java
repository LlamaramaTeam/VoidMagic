package io.github.llamarama.team.voidmagic.common.tile;

import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
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

public class ScrollTileEntity extends TileEntity implements ITickableTileEntity {

    public ScrollTileEntity() {
        super(ModTileEntityTypes.SCROLL.get());
    }

    @Override
    public void tick() {
        if (this.world == null)
            return;

        if (!this.world.isRemote()) {
            // Server logic
            BlockState currentState = this.getBlockState();
            if (currentState.get(ModBlockProperties.OPEN) && currentState.isValidPosition(this.world, this.getPos()))
                this.world.destroyBlock(this.getPos(), true);
        } else {
            // Maybe some client logic
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
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

}
