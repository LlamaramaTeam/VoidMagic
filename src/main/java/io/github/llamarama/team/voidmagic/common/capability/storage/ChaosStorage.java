package io.github.llamarama.team.voidmagic.common.capability.storage;

import io.github.llamarama.team.voidmagic.api.capability.IChaosHandler;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ChaosStorage implements Capability.IStorage<IChaosHandler> {

    @Override
    public INBT writeNBT(Capability<IChaosHandler> capability, IChaosHandler instance, @Nullable Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(NBTConstants.CHAOS, instance.getChaos());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IChaosHandler> capability, IChaosHandler instance, @Nullable Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        int chaos = tag.getInt(NBTConstants.CHAOS);

        instance.setChaos(chaos);
    }

}
