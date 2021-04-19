package io.github.llamarama.team.voidmagic.common.capability.provider;

import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import io.github.llamarama.team.voidmagic.common.capability.impl.ChaosHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChaosEntityProvider implements ICapabilitySerializable<CompoundNBT> {

    private final ChaosHandler handler = new ChaosHandler();
    private final LazyOptional<IChaosHandler> chaos = LazyOptional.of(() -> handler);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return VoidMagicCapabilities.CHAOS.orEmpty(cap, this.chaos);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) VoidMagicCapabilities.CHAOS.writeNBT(this.handler, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        VoidMagicCapabilities.CHAOS.readNBT(this.handler, null, nbt);
    }

}
