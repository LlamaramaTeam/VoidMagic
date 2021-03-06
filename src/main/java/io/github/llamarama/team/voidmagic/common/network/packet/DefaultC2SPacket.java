package io.github.llamarama.team.voidmagic.common.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class DefaultC2SPacket extends GenericPacket {


    public DefaultC2SPacket(Identifier id, PacketByteBuf buffer) {
        super(id, buffer);
    }

    public DefaultC2SPacket(Identifier id, MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        super(id);
    }

}
