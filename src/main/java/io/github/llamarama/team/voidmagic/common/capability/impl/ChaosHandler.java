package io.github.llamarama.team.voidmagic.common.capability.impl;

import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class ChaosHandler implements IChaosHandler, INBTSerializable<CompoundNBT> {

    public static final ChaosHandler DEFAULT = new ChaosHandler(1000, new Random());

    private int chaos;
    private int maxChaos;

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
        if (newVal + this.chaos > this.maxChaos) {
            throw new IllegalArgumentException("Can't overload the chunks chaos more than its max size!");
        }

        this.chaos = newVal;
        this.markDirty();
    }

    public void markDirty() {

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
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt(NBTConstants.MAX_CHAOS, this.maxChaos);
        tag.putInt(NBTConstants.CHAOS, this.chaos);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.maxChaos = nbt.getInt(NBTConstants.MAX_CHAOS);
        this.chaos = nbt.getInt(NBTConstants.CHAOS);
    }

}
