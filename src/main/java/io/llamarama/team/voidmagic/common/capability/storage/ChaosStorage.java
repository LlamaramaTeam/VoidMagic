package io.llamarama.team.voidmagic.common.capability.storage;

import io.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import io.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

public class ChaosStorage implements Capability.IStorage<IChaosHandler> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IChaosHandler> capability, IChaosHandler instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt(NBTConstants.CHAOS, instance.getChaos());
        tag.putInt(NBTConstants.MAX_CHAOS, instance.getMaxChaos());

        return tag;
    }

    @Override
    public void readNBT(Capability<IChaosHandler> capability, IChaosHandler instance, Direction side, INBT nbt) {
        CompoundNBT tag = ((CompoundNBT) nbt);

        int currect = tag.getInt(NBTConstants.CHAOS);
        int max = tag.getInt(NBTConstants.MAX_CHAOS);

        instance.setChaos(currect);
        instance.setMaxChaos(max);
    }

}
