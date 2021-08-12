package io.github.llamarama.team.voidmagic.common.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class GenericPacket implements Packet {

    private final Identifier id;
    private final PacketByteBuf buffer;

    public GenericPacket(Identifier id, PacketByteBuf buffer) {
        this.id = id;
        this.buffer = buffer;
    }

    public GenericPacket(Identifier id) {
        this.id = id;
        this.buffer = PacketByteBufs.empty();
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public PacketByteBuf getBuffer() {
        return this.buffer;
    }

}
