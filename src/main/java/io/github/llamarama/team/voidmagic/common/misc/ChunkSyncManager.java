package io.github.llamarama.team.voidmagic.common.misc;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCaps;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.network.packet.MassChunkUpdatePacket;
import io.github.llamarama.team.voidmagic.util.constants.IntegerConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ChunkSyncManager {

    public static ChunkSyncManager INSTANCE;
    private final SetMultimap<UUID, ChunkPos> pending;

    public ChunkSyncManager() {
        this.pending = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);

        VoidMagic.getLogger().info("Created chunk sync manager for server.");
    }

    public void push(ChunkPos chunkPos, PlayerEntity player) {
        if (this.pending.containsEntry(player.getUniqueID(), chunkPos))
            return;

        UUID uniqueID = player.getUniqueID();
        boolean put = this.pending.put(uniqueID, chunkPos);
        VoidMagic.getLogger().debug(put);
        if (put)
            VoidMagic.getLogger().debug("Pushed chunk at " + chunkPos.x + ", " + chunkPos.z);
    }

    public void pop(PlayerEntity player, ChunkPos chunkPos) {
        this.pop(player.getUniqueID(), chunkPos);
    }

    public void pop(UUID uuid, ChunkPos pos) {
        if (this.pending.containsEntry(uuid, pos))
            this.pending.remove(uuid, pos);
    }

    public void popKey(UUID uuid) {
        if (this.pending.containsKey(uuid)) {
            this.pending.removeAll(uuid);
        }
    }

    public void popAll() {
        this.pending.clear();
    }

    public void sendStatus(ServerPlayerEntity player) {
        VoidMagic.getLogger().debug("Status sent to " + player);
        ServerWorld world = player.getServerWorld();
        HashMap<ChunkPos, Integer> toBSent = new HashMap<>();

        if (!this.pending.containsKey(player.getUniqueID()))
            return;

        this.pending.values().forEach((chunkPos) -> {
            Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
            chunk.getCapability(VoidMagicCaps.CHAOS).ifPresent(
                    (chaosHandler) -> toBSent.put(chunkPos, chaosHandler.getChaos()));
        });

        ModNetworking.get().sendToClient(new MassChunkUpdatePacket(toBSent), player);
    }

    public void enqueue(Runnable runnable) {
        if (this.pending.size() > IntegerConstants.MIN_RENDER_DISTANCE)
            runnable.run();
    }

}
