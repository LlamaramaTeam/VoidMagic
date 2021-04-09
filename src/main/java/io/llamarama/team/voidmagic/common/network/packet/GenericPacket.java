package io.llamarama.team.voidmagic.common.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class GenericPacket implements IPacket {

    private final Function<Supplier<NetworkEvent.Context>, Boolean> function;

    protected GenericPacket(Function<Supplier<NetworkEvent.Context>, Boolean> contextSupplier) {
        this.function = contextSupplier;
    }


    @Override
    public void encode(PacketBuffer buffer) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        return function.apply(contextSupplier);
    }

}
