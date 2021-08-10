package io.github.llamarama.team.common.tile;

import io.github.llamarama.team.api.tile.OfferingPlateInventory;
import io.github.llamarama.team.common.register.ModBlockEntityTypes;
import io.github.llamarama.team.common.util.InventoryUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class OfferingPlateBlockEntity extends BlockEntity implements OfferingPlateInventory, BlockEntityClientSerializable {

    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
    @Environment(EnvType.CLIENT)
    public float rotationTick;

    public OfferingPlateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.OFFERING_PLATE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getStacks() {
        return this.stacks;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        InventoryUtils.writeInventory(nbt, this.stacks);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        InventoryUtils.readInventory(nbt, this.stacks);
    }

    public void interact(@NotNull ServerPlayerEntity player) {
        ItemStack playerStack = player.getStackInHand(player.getActiveHand());
        ItemStack stackInHand = playerStack.copy();
        ItemStack stackInSlot = this.removeStack(0);

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

        this.sync();
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        this.readNbt(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return this.writeNbt(tag);
    }

}
