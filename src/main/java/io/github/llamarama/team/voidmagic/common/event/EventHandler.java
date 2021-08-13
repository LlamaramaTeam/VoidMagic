package io.github.llamarama.team.voidmagic.common.event;

import io.github.llamarama.team.voidmagic.common.event.custom.ChunkWatchEvents;
import io.github.llamarama.team.voidmagic.common.lib.chaos.ChunkSyncManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

public final class EventHandler {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register(EventHandler::onPlayerJoined);
        ServerPlayConnectionEvents.DISCONNECT.register(EventHandler::onPlayerDisconnect);
        ServerLifecycleEvents.SERVER_STARTING.register(EventHandler::onServerStart);
        ServerLifecycleEvents.SERVER_STOPPING.register(EventHandler::onServerStop);
        ServerTickEvents.END_WORLD_TICK.register(EventHandler::onWorldTick);
        ChunkWatchEvents.CHUNK_WATCHED_BY_PLAYER.register(EventHandler::onChunkWatched);
        ChunkWatchEvents.CHUNK_UNWATCHED_BY_PLAYER.register(EventHandler::onChunkUnWatched);
    }

    private static void onChunkUnWatched(ServerPlayerEntity serverPlayerEntity, ChunkPos chunkPos) {
        ChunkSyncManager.INSTANCE.pop(serverPlayerEntity, chunkPos);
    }

    private static void onChunkWatched(ServerPlayerEntity serverPlayerEntity, ChunkPos chunkPos) {
        ChunkSyncManager.INSTANCE.push(chunkPos, serverPlayerEntity);
    }

    private static void onWorldTick(ServerWorld world) {
        ChunkSyncManager.INSTANCE.enqueue(() ->
                world.getPlayers().forEach((playerEntity) -> ChunkSyncManager.INSTANCE.sendStatus(playerEntity))
        );
    }

    private static void onServerStart(MinecraftServer server) {
        // Create a chunk sync manager for the server.
        ChunkSyncManager.INSTANCE = new ChunkSyncManager();
    }

    private static void onServerStop(MinecraftServer server) {
        // Remove the chunk sync manager from memory to prevent unecessary garbage collection.
        ChunkSyncManager.INSTANCE.popAll();
    }

    private static void onPlayerJoined(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender sender, MinecraftServer server) {

    }

    private static void onPlayerDisconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer server) {
        ChunkSyncManager.INSTANCE.popKey(serverPlayNetworkHandler.getPlayer().getUuid());
    }

}
