package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ReduceChaosPacket extends GenericPacket implements IPacket {

    private final int amount;

    public ReduceChaosPacket(int amount) {
        super();
        this.amount = amount;
    }

    public ReduceChaosPacket(PacketBuffer buffer) {
        super(buffer);
        this.amount = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
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
            BlockPos position = sender.getPosition();
            ServerWorld serverWorld = sender.getServerWorld();
            LazyOptional<IChaosHandler> capability = serverWorld.getChunkAt(position).getCapability(VoidMagicCapabilities.CHAOS);

            capability.ifPresent((chaosHandler) -> chaosHandler.consume(this.amount));
        });

        contextSupplier.get().setPacketHandled(result.get());
        return result.get();
    }

}
