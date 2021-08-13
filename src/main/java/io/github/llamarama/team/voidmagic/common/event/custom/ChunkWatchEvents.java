package io.github.llamarama.team.voidmagic.common.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;

public final class ChunkWatchEvents {

    public static final Event<Watch> CHUNK_WATCHED_BY_PLAYER =
            EventFactory.createArrayBacked(ChunkWatchEvents.Watch.class, (callbacks) -> (serverPlayer, chunkPos) -> {
                for (Watch callback : callbacks) {
                    callback.onChunkWatched(serverPlayer, chunkPos);
                }
            });

    public static final Event<UnWatch> CHUNK_UNWATCHED_BY_PLAYER =
            EventFactory.createArrayBacked(ChunkWatchEvents.UnWatch.class, callbacks -> (serverPlayer, chunkPos) -> {
                for (UnWatch callback : callbacks) {
                    callback.onChunkUnWatched(serverPlayer, chunkPos);
                }
            });

    @FunctionalInterface
    public interface Watch {

        void onChunkWatched(ServerPlayerEntity player, ChunkPos chunkPos);

    }

    @FunctionalInterface
    public interface UnWatch {

        void onChunkUnWatched(ServerPlayerEntity player, ChunkPos chunkPos);

    }

}
