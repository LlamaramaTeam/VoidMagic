package io.github.llamarama.team.voidmagic.common.capability.impl;

import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class ChaosHandler implements IChaosHandler, INBTSerializable<CompoundNBT> {

    private int chaos;
    private Runnable markDirty;

    public ChaosHandler() {
        this(1000);
    }

    public ChaosHandler(int chaos) {
        this.chaos = chaos;
    }

    @Override
    public int getChaos() {
        return this.chaos;
    }

    @Override
    public void setChaos(int newVal) {
        this.chaos = newVal;
        this.markDirty.run();
    }

    /**
     * @param markDirty Has to be the mark dirty method of the
     *                  {@link net.minecraftforge.common.capabilities.ICapabilityProvider} used.
     */
    public void onChangeRun(Runnable markDirty) {
        this.markDirty = markDirty;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) VoidMagicCapabilities.CHAOS.writeNBT(this, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        VoidMagicCapabilities.CHAOS.readNBT(this, null, nbt);
    }

}
