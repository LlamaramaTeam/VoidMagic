package io.github.llamarama.team.voidmagic.common.capability.provider;

import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCaps;
import io.github.llamarama.team.voidmagic.common.capability.impl.ChaosHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChaosChunkProvider implements ICapabilitySerializable<CompoundNBT> {

    private final ChaosHandler handler = new ChaosHandler();
    private final LazyOptional<ChaosHandler> chaos = LazyOptional.of(() -> handler);

    public ChaosChunkProvider(Chunk chunk) {
        this.handler.onChangeRun(chunk::markDirty);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == VoidMagicCaps.CHAOS ? this.chaos.cast() : LazyOptional.empty();
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
