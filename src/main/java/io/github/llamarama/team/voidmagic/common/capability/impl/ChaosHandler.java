package io.github.llamarama.team.voidmagic.common.capability.impl;

import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Random;

public class ChaosHandler implements IChaosHandler, INBTSerializable<CompoundNBT> {

    private int chaos;
    private int maxChaos;
    private Runnable markDirty;

    public ChaosHandler() {
        this(1000, new Random());
    }

    public ChaosHandler(int maxChaos, Random random) {
        this.maxChaos = maxChaos;
        this.chaos = MathHelper.nextInt(random, maxChaos / 3, maxChaos);
    }

    @Override
    public int getChaos() {
        return this.chaos;
    }

    @Override
    public void setChaos(int newVal) {
        this.chaos = newVal;
        this.markDirty();
    }


    // Do NOT override this method.
    @Deprecated
    public void markDirty() {
        this.markDirty.run();
    }

    /**
     * @param markDirty Has to be the mark dirty method of the
     *                  {@link net.minecraftforge.common.capabilities.ICapabilityProvider} used.
     * @return This instance.
     */
    @Nonnull
    public ChaosHandler onChangeRun(Runnable markDirty) {
        this.markDirty = markDirty;

        return this;
    }

    @Override
    public int getMaxChaos() {
        return this.maxChaos;
    }

    /**
     * @Deprecated Do NOT call this method.
     */
    @Deprecated
    @Override
    public void setMaxChaos(int max) {
        this.maxChaos = max;
        this.markDirty();
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
