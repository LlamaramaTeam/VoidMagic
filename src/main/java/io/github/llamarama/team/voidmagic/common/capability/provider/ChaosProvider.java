package io.github.llamarama.team.voidmagic.common.capability.provider;

import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import io.github.llamarama.team.voidmagic.common.capability.impl.ChaosHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ChaosProvider implements ICapabilitySerializable<CompoundNBT> {

    public static final ChaosHandler handler = new ChaosHandler(1000, new Random());
    public static final LazyOptional<ChaosHandler> chaos = LazyOptional.of(() -> handler);

    public static void invalidate() {
        chaos.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == VoidMagicCapabilities.CHAOS) {
            return chaos.cast();
        }

        return LazyOptional.empty().cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        handler.deserializeNBT(nbt);
    }

}
