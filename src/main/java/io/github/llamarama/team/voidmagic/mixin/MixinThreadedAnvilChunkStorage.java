package io.github.llamarama.team.voidmagic.mixin;

import io.github.llamarama.team.voidmagic.common.event.custom.ChunkWatchEvents;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class MixinThreadedAnvilChunkStorage {

    @Shadow
    @Final
    ServerWorld world;

    @Inject(method = "sendWatchPackets", at = @At("HEAD"))
    private void fireEvent(ServerPlayerEntity player, ChunkPos pos, Packet<?>[] packets, boolean withinMaxWatchDistance, boolean withinViewDistance, CallbackInfo ci) {
        if (player.world == this.world) {
            if (withinMaxWatchDistance != withinViewDistance) {
                if (withinViewDistance) {
                    ChunkWatchEvents.CHUNK_WATCHED_BY_PLAYER.invoker().onChunkWatched(player, pos);
                } else {
                    ChunkWatchEvents.CHUNK_UNWATCHED_BY_PLAYER.invoker().onChunkUnWatched(player, pos);
                }
            }
        }
    }

}
