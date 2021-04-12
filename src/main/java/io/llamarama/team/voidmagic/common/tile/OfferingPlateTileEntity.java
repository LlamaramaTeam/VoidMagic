package io.llamarama.team.voidmagic.common.tile;

import io.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import io.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OfferingPlateTileEntity extends TileEntity {

    private final ItemStackHandler items = new ItemStackHandler(NonNullList.withSize(1, ItemStack.EMPTY)) {
        @Override
        protected void onContentsChanged(int slot) {
            OfferingPlateTileEntity.this.markDirty();
            super.onContentsChanged(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (this.getStackInSlot(0).getCount() == 1) {
                return ItemStack.EMPTY;
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return super.isItemValid(slot, stack) && this.getStackInSlot(0).getCount() != 1;
        }
    };
    private final LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> items);
    public int rotationTick;

    public OfferingPlateTileEntity() {
        super(ModTileEntityTypes.OFFERING_PLATE.get());
    }

    public void interact(ServerPlayerEntity player) {
        ItemStack heldItem = player.getHeldItem(player.getActiveHand()).copy();
        ItemStack stackInSlot = this.items.extractItem(0, 1, false);
        if (stackInSlot.isEmpty()) {
            heldItem.setCount(1);
            player.getHeldItem(player.getActiveHand()).shrink(1);
            this.items.insertItem(0, heldItem, false);
        } else {
            if (!player.addItemStackToInventory(stackInSlot)) {
                Vector3d pos = Vector3d.copyCentered(this.getPos()).add(0.5f, 0.5f, 0.5f);
                player.world.addEntity(new ItemEntity(player.world, pos.x, pos.y, pos.z, stackInSlot));
            }
        }
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
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.items.deserializeNBT(nbt.getCompound(NBTConstants.INVENTORY));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put(NBTConstants.INVENTORY, this.items.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void remove() {
        super.remove();
        itemOptional.invalidate();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = super.getUpdateTag();
        updateTag.put(NBTConstants.INVENTORY, this.items.serializeNBT());
        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockPos pos = pkt.getPos();
        CompoundNBT nbt = pkt.getNbtCompound();

        if (this.world != null) {
            this.handleUpdateTag(this.world.getBlockState(pos), nbt);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 2, 1));
    }

}
