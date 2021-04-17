package io.github.llamarama.team.voidmagic.common.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class GenericPacket implements IPacket {

    public GenericPacket() {

    }

    public GenericPacket(PacketBuffer buffer) {
        this();
    }

    @Override
    public abstract void encode(PacketBuffer buffer);

    @Override
    public abstract boolean handle(Supplier<NetworkEvent.Context> contextSupplier);

}
