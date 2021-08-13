package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.common.util.ChaosUtils;
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

import java.util.HashMap;

public class MassChunkUpdatePacket extends DefaultS2CPacket {

    public static final Identifier MASS_CHUNK_UPDATE_PACKET_ID = IdBuilder.of("mass_chunk_update");

    public MassChunkUpdatePacket(HashMap<ChunkPos, Integer> chunkMap) {
        super(MASS_CHUNK_UPDATE_PACKET_ID, PacketByteBufs.create());
        PacketByteBuf buffer = this.getBuffer();

        int size = chunkMap.size();
        buffer.writeInt(size);

        for (ChunkPos current : chunkMap.keySet()) {
            buffer.writeChunkPos(current);
            buffer.writeInt(chunkMap.get(current));
        }

//        VoidMagic.getLogger().info("Chunk update packet sent.");
    }

    public MassChunkUpdatePacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        super(MASS_CHUNK_UPDATE_PACKET_ID, client, handler, buf, responseSender);
//        VoidMagic.getLogger().info("Packet received.");


        int size = buf.readInt();
        HashMap<ChunkPos, Integer> read = new HashMap<>();

        for (int i = 0; i < size; i++) {
            ChunkPos current = buf.readChunkPos();
            int amount = buf.readInt();

            read.put(current, amount);
        }
//        VoidMagic.getLogger().info("Packet decoded.");

        client.execute(() -> {
            ClientWorld world = client.world;
            if (world != null) {
                read.forEach((chunkPos, chaosValue) -> {
                    Chunk current = world.getChunk(chunkPos.x, chunkPos.z);

                    ChaosUtils.executeForChaos(current, (chaosHandler) -> chaosHandler.setChaosValue(chaosValue));
                });
//                VoidMagic.getLogger().info("Packet handled.");
            }
        });
    }

}
