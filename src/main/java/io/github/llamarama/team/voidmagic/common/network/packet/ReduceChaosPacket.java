package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.util.ChaosUtils;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

public class ReduceChaosPacket extends DefaultC2SPacket {

    public static final Identifier REDUCE_CHAOS_PACKET_ID = IdBuilder.of("reduce_chaos");

    public ReduceChaosPacket(ChunkPos chunkPos, int amount) {
        super(REDUCE_CHAOS_PACKET_ID, PacketByteBufs.create());
        PacketByteBuf buffer = this.getBuffer();

        buffer.writeInt(amount);
        buffer.writeChunkPos(chunkPos);
    }

    public ReduceChaosPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        super(REDUCE_CHAOS_PACKET_ID, server, player, handler, buf, responseSender);
        int amount = buf.readInt();
        ChunkPos chunkPos = buf.readChunkPos();
        ServerWorld world = player.getServerWorld();
        WorldChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);

        server.execute(() -> ChaosUtils.executeForChaos(chunk, chaosProvider -> {
            chaosProvider.consume(amount);
            ModNetworking.get().sendToAllClose(new ChunkChaosUpdatePacket(chunk), world, chunk.getPos().getStartPos());
        }));
    }

}
