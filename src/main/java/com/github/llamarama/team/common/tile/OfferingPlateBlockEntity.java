package com.github.llamarama.team.common.tile;

import com.github.llamarama.team.api.tile.OfferingPlateInventory;
import com.github.llamarama.team.common.register.ModBlockEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OfferingPlateBlockEntity extends BlockEntity implements OfferingPlateInventory, BlockEntityClientSerializable {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    @Environment(EnvType.CLIENT)
    public float rotationTick;

    public OfferingPlateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.OFFERING_PLATE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getStacks() {
        return this.inventory;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    public void interact(@NotNull ServerPlayerEntity player) {
        ItemStack playerStack = player.getStackInHand(player.getActiveHand());
        ItemStack stackInHand = playerStack.copy();
        ItemStack stackInSlot = this.removeStack(0, 1);

        if (stackInSlot.isEmpty()) {
            stackInHand.setCount(1);
            playerStack.decrement(1);
            this.setStack(0, stackInHand);
        } else {
            if (!player.giveItemStack(stackInSlot)) {
                Vec3d pos = Vec3d.ofCenter(this.getPos()).add(0.5, 0.5, 0.5);
                player.getServerWorld().spawnEntity(new ItemEntity(player.world, pos.x, pos.y, pos.z, stackInSlot));
            }
        }
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.getPos(), -1, this.toInitialChunkDataNbt());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.writeNbt(new NbtCompound());
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        this.readNbt(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return this.writeNbt(tag);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.sync();
    }

}
