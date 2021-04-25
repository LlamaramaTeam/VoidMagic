package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCaps;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class IncreaseChaosPacket extends GenericPacket {

    private final ChunkPos chunkPos;
    private final int amount;

    public IncreaseChaosPacket(ChunkPos chunkPos, int amount) {
        super();
        this.chunkPos = chunkPos;
        this.amount = amount;
    }

    public IncreaseChaosPacket(PacketBuffer buffer) {
        super(buffer);
        int x = buffer.readInt();
        int z = buffer.readInt();
        this.chunkPos = new ChunkPos(x, z);
        this.amount = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.chunkPos.x);
        buffer.writeInt(this.chunkPos.z);
        buffer.writeInt(this.amount);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier, AtomicBoolean result) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = contextSupplier.get().getSender();
            if (player == null) {
                result.set(false);
                return;
            }
            Chunk chunk = player.getServerWorld().getChunk(this.chunkPos.x, this.chunkPos.z);

            chunk.getCapability(VoidMagicCaps.CHAOS).ifPresent((chaosHandler) -> {
                chaosHandler.increase(this.amount);
                ModNetworking.get().sendToAll(new ChunkChaosUpdatePacket(chunk), player.getServerWorld());
            });
        });

        contextSupplier.get().setPacketHandled(result.get());
        return result.get();
    }

}
