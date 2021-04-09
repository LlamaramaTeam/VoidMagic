package io.llamarama.team.voidmagic.common.network;

import io.llamarama.team.voidmagic.common.network.packet.IPacket;
import io.llamarama.team.voidmagic.common.network.packet.OpenScreenPacket;
import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworking {

    public static SimpleChannel CHANNEL;
    // Do not touch.
    private static int id = 0;

    public static void initialize() {
        CHANNEL = NetworkRegistry.newSimpleChannel(StringConstants.Network.CHANNEL_ID.getId(),
                StringConstants.NETWORK_PROTOCOL_VERSION::get,
                StringConstants.NETWORK_PROTOCOL_VERSION.get()::equals,
                StringConstants.NETWORK_PROTOCOL_VERSION.get()::equals);

        registerPacket(new OpenScreenPacket());
    }

    public static void sendToClient(IPacket packet, ServerPlayerEntity playerEntity) {
        CHANNEL.sendTo(packet, playerEntity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAll(IPacket packet, ServerWorld world) {
        world.getPlayers().forEach((serverPlayerEntity) -> sendToClient(packet, serverPlayerEntity));
    }

    public static void sendToServer(IPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    private static void registerPacket(IPacket packet) {
        CHANNEL.messageBuilder(packet.getClass(), id++)
                .encoder(packet::encode)
                .decoder(packet::decode)
                .consumer(IPacket::handle);
    }

}
