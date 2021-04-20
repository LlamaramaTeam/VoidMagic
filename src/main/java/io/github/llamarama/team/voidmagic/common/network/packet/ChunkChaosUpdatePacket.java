package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.capability.CapUtils;
import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ChunkChaosUpdatePacket extends GenericPacket {

    @Nullable
    private final Chunk chunk;
    private int updatedVal;

    public ChunkChaosUpdatePacket(@Nullable Chunk chunk) {
        super();
        this.chunk = chunk;
    }

    public ChunkChaosUpdatePacket(PacketBuffer buffer) {
        super(buffer);

        int x = buffer.readInt();
        int y = buffer.readInt();

        this.updatedVal = buffer.readInt();

        if (FMLEnvironment.dist != Dist.CLIENT) {
            this.chunk = null;
            return;
        }

        ClientWorld world = VoidMagicClient.getGame().world;
        if (world == null) {
            this.chunk = null;
            return;
        }

        this.chunk = world.getChunk(x, y);
        VoidMagic.getLogger().debug("Received packet.");
    }

    @Override
    public void encode(PacketBuffer buffer) {
        if (this.chunk != null) {
            int x = this.chunk.getPos().x;
            int z = this.chunk.getPos().z;

            buffer.writeInt(x);
            buffer.writeInt(z);
            final AtomicInteger updatedChaos = new AtomicInteger(0);
            this.chunk.getCapability(VoidMagicCapabilities.CHAOS).ifPresent(
                    (chaosHandler) -> updatedChaos.set(chaosHandler.getChaos())
            );

            buffer.writeInt(updatedChaos.get());
        }
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        final AtomicBoolean out = new AtomicBoolean(true);

        contextSupplier.get().enqueueWork(() -> {
            if (this.chunk == null) {
                out.set(false);
                return;
            }

            CapUtils.executeForChaos(chunk, (chaosHandler) -> chaosHandler.setChaos(this.updatedVal));
        });

        contextSupplier.get().setPacketHandled(out.get());
        return out.get();
    }

}
