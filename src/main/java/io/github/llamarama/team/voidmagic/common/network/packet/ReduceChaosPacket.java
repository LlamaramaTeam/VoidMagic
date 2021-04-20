package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCaps;
import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ReduceChaosPacket extends GenericPacket implements IPacket {

    private final int amount;
    private final int x, z;

    public ReduceChaosPacket(int amount, Chunk chunk) {
        super();
        this.amount = amount;
        ChunkPos pos = chunk.getPos();

        this.x = pos.x;
        this.z = pos.z;
    }

    public ReduceChaosPacket(PacketBuffer buffer) {
        super(buffer);
        this.x = buffer.readInt();
        this.z = buffer.readInt();
        this.amount = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(z);
        buffer.writeInt(this.amount);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        final AtomicBoolean result = new AtomicBoolean(true);
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayerEntity sender = contextSupplier.get().getSender();
            if (sender == null) {
                result.set(false);
                return;
            }
            VoidMagic.getLogger().info("Starting processing of packet!");
            ServerWorld serverWorld = sender.getServerWorld();

            Chunk chunk = serverWorld.getChunk(this.x, this.z);
            LazyOptional<IChaosHandler> capability =
                    chunk.getCapability(VoidMagicCaps.CHAOS);

            sender.sendMessage(
                    new StringTextComponent(Integer.toString(this.amount)), sender.getUniqueID());
            capability.ifPresent((chaosHandler) -> {
                chaosHandler.consume(this.amount);
                ModNetworking.get().sendToAll(new ChunkChaosUpdatePacket(chunk), serverWorld);
            });
        });

        contextSupplier.get().setPacketHandled(result.get());
        return result.get();
    }

}
