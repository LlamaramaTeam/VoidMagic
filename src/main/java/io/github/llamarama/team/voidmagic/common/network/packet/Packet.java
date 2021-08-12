package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface Packet {

    Identifier getId();

    PacketByteBuf getBuffer();

    default void sendToClient(ServerPlayerEntity playerEntity) {
        ModNetworking.get().sendToClient(this, playerEntity);
    }

}
