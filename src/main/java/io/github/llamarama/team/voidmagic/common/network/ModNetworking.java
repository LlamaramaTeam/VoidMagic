package io.github.llamarama.team.voidmagic.common.network;

import io.github.llamarama.team.voidmagic.common.network.packet.*;
import io.github.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Function;

public class ModNetworking {

    // Do not touch.
    private static int id = 0;
    private static ModNetworking instance;
    private SimpleChannel CHANNEL;

    private ModNetworking() {

    }

    public static ModNetworking get() {
        if (instance == null) {
            instance = new ModNetworking();
        }

        return instance;
    }

    public void sendToClient(IPacket packet, ServerPlayerEntity playerEntity) {
        CHANNEL.sendTo(packet, playerEntity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public void sendToAll(IPacket packet, ServerWorld world) {
        world.getPlayers().forEach((serverPlayerEntity) -> this.sendToClient(packet, serverPlayerEntity));
    }

    public void sendToServer(IPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    private <PCT extends IPacket> void registerPacket(Class<PCT> packetClass, Function<PacketBuffer, PCT> decoder) {
        CHANNEL.messageBuilder(packetClass, id++)
                .encoder(IPacket::encode)
                .decoder(decoder)
                .consumer(IPacket::handle)
                .add();
    }

    public void initialize() {
        CHANNEL = NetworkRegistry.newSimpleChannel(StringConstants.Network.CHANNEL_ID.getId(),
                StringConstants.NETWORK_PROTOCOL_VERSION::get,
                StringConstants.NETWORK_PROTOCOL_VERSION.get()::equals,
                StringConstants.NETWORK_PROTOCOL_VERSION.get()::equals);
        this.registerPackets();
    }

    private void registerPackets() {
        this.registerPacket(SendChatMessagePacket.class, SendChatMessagePacket::new);
        this.registerPacket(ReduceChaosPacket.class, ReduceChaosPacket::new);
        this.registerPacket(ChunkChaosUpdatePacket.class, ChunkChaosUpdatePacket::new);
        this.registerPacket(OpenBookScreenPacket.class, OpenBookScreenPacket::new);
    }

}
