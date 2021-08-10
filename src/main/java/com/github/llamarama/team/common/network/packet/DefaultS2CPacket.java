package com.github.llamarama.team.common.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class DefaultS2CPacket extends GenericPacket {

    public DefaultS2CPacket(Identifier id, PacketByteBuf buffer) {
        super(id, buffer);
    }

    public DefaultS2CPacket(Identifier id, MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        super(id);
    }

}
