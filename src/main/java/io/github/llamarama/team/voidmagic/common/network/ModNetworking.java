package io.github.llamarama.team.voidmagic.common.network;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.network.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class ModNetworking {

    private static ModNetworking INSTANCE;

    public static ModNetworking get() {
        if (INSTANCE == null) {
            INSTANCE = new ModNetworking();
        }

        return INSTANCE;
    }

    public static void respond(PacketSender sender, Packet packet) {
        sender.sendPacket(packet.getId(), packet.getBuffer());
    }

    public void sendToClient(Packet packet, ServerPlayerEntity playerEntity) {
        ServerPlayNetworking.send(playerEntity, packet.getId(), packet.getBuffer());
    }

    public void sendToAllClose(Packet packet, ServerWorld world, BlockPos targetPos) {
        Collection<ServerPlayerEntity> targetPlayers = PlayerLookup.tracking(world, targetPos);
        targetPlayers.forEach(packet::sendToClient);
    }

    public void sendToAll(Packet packet, ServerWorld world) {
        world.getPlayers().forEach(player -> ServerPlayNetworking.send(player, packet.getId(), packet.getBuffer()));
    }

    public void sendToServer(Packet packet) {
        ClientPlayNetworking.send(packet.getId(), packet.getBuffer());
    }

    private void registerClientBound(Identifier id, ClientPlayNetworking.PlayChannelHandler handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, handler);
    }

    private void registerServerBound(Identifier id, ServerPlayNetworking.PlayChannelHandler handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, handler);
    }

    public void init() {
        this.registerClientBound(OpenGuideBookPacket.OPEN_BOOK_PACKET_ID, OpenGuideBookPacket::new);
        this.registerServerBound(ReduceChaosPacket.REDUCE_CHAOS_PACKET_ID, ReduceChaosPacket::new);
        this.registerServerBound(IncreaseChaosPacket.INCREASE_CHAOS_PACKET_ID, IncreaseChaosPacket::new);
        this.registerClientBound(ChunkChaosUpdatePacket.CHUNK_CHAOS_UPDATE_PACKET_ID, ChunkChaosUpdatePacket::new);
        this.registerClientBound(MassChunkUpdatePacket.MASS_CHUNK_UPDATE_PACKET_ID, MassChunkUpdatePacket::new);

        VoidMagic.getLogger().info("Succssfully registered packets for VoidMagic");
    }

}
