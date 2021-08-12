package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.common.lib.chaos.ChaosProvider;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

public class ChunkChaosUpdatePacket extends DefaultS2CPacket {

    public static final Identifier CHUNK_CHAOS_UPDATE_PACKET_ID = IdBuilder.of("chunk_chaos_update");

    public ChunkChaosUpdatePacket(Chunk chunk) {
        super(CHUNK_CHAOS_UPDATE_PACKET_ID, PacketByteBufs.create());

        PacketByteBuf buffer = this.getBuffer();

        ChunkPos pos = chunk.getPos();
        buffer.writeInt(pos.x);
        buffer.writeInt(pos.z);
        if (chunk instanceof ChaosProvider)
            buffer.writeInt(((ChaosProvider) chunk).getChaosValue());
    }

    public ChunkChaosUpdatePacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        super(CHUNK_CHAOS_UPDATE_PACKET_ID, client, handler, buf, responseSender);
        int x = buf.readInt();
        int z = buf.readInt();

        int chaosValue = buf.readInt();

        ClientWorld world = client.world;

        client.execute(() -> {
            if (world != null) {
                WorldChunk chunk = world.getChunk(x, z);

                if (chunk instanceof ChaosProvider)
                    ((ChaosProvider) chunk).setChaosValue(chaosValue);
            }
        });
    }

}
