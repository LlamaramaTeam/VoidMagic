package io.github.llamarama.team.voidmagic.common.capability.provider;

import io.github.llamarama.team.voidmagic.common.capability.impl.ChaosHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChaosProvider implements ICapabilitySerializable<CompoundNBT> {

    private final LazyOptional<ChaosHandler> chaos;
    private final ChaosHandler handler;

    public ChaosProvider(Chunk chunk) {
        this.handler = new ChaosHandler().onChangeRun(chunk::markDirty);
        this.chaos = LazyOptional.of(() -> this.handler);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return this.chaos.cast();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.chaos.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        handler.deserializeNBT(nbt);
    }

    public void invalidate() {
        this.chaos.invalidate();
    }

}
