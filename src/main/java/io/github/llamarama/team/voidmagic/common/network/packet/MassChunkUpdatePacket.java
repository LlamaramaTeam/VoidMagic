package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.capability.CapUtils;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class MassChunkUpdatePacket extends GenericPacket {

    private final HashMap<ChunkPos, Integer> posCollection;

    public MassChunkUpdatePacket(HashMap<ChunkPos, Integer> posHashMap) {
        super();
        if (posHashMap.isEmpty())
            throw new IllegalArgumentException("Cannot parse an empty hashmap");

        this.posCollection = posHashMap;
    }

    public MassChunkUpdatePacket(PacketBuffer buffer) {
        super(buffer);

        int size = buffer.readInt();
        HashMap<ChunkPos, Integer> read = new HashMap<>();

        for (int i = 0; i < size; i++) {
            int x = buffer.readInt();
            int z = buffer.readInt();
            int amount = buffer.readInt();

            read.put(new ChunkPos(x, z), amount);
        }

        this.posCollection = read;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        int size = posCollection.size();
        buffer.writeInt(size);

        for (ChunkPos current : posCollection.keySet()) {
            buffer.writeInt(current.x);
            buffer.writeInt(current.z);
            buffer.writeInt(this.posCollection.get(current));
        }
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier, AtomicBoolean result) {
        contextSupplier.get().enqueueWork(() -> {
            ClientWorld world = VoidMagicClient.getGame().world;
            if (world != null) {
                this.posCollection.keySet().forEach((chunkPos) -> {
                    Chunk current = world.getChunk(chunkPos.x, chunkPos.z);
                    int newVal = this.posCollection.get(chunkPos);

                    CapUtils.executeForChaos(current, (chaosHandler) -> chaosHandler.setChaos(newVal));
                });
            } else {
                result.set(false);
            }
        });
        VoidMagic.getLogger().debug("Tried Handling Packet");
        contextSupplier.get().setPacketHandled(result.get());
        return result.get();
    }

}
