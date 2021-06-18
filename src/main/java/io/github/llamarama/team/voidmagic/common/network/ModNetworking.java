package io.github.llamarama.team.voidmagic.common.network;

import io.github.llamarama.team.voidmagic.common.network.packet.*;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import io.github.llamarama.team.voidmagic.common.util.constants.ModConstants;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModNetworking {

    // Do not touch.
    private static int id = 0;
    private static ModNetworking instance;
    private SimpleChannel CHANNEL;

    private ModNetworking() {
    }

    public static ModNetworking get() {
        if (instance == null) {
            instance = new ModNetworking();
        }

        return instance;
    }

    public void sendToClient(IPacket packet, ServerPlayerEntity playerEntity) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), packet);
    }

    public void sendToAll(IPacket packet, ServerWorld world) {
        world.getPlayers().forEach((serverPlayerEntity) -> this.sendToClient(packet, serverPlayerEntity));
    }

    @SuppressWarnings("unused")
    public void sendToServer(IPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    private <PCT extends IPacket> void registerPacket(Class<PCT> packetClass) {
        CHANNEL.messageBuilder(packetClass, id++)
                .encoder(IPacket::encode)
                .decoder((buffer) -> {
                    try {
                        return packetClass.getConstructor(buffer.getClass()).newInstance(buffer);
                    } catch (InvocationTargetException |
                            InstantiationException |
                            IllegalAccessException |
                            NoSuchMethodException e) {
                        e.printStackTrace();
                        throw new IllegalStateException("Could not find class with name: " + packetClass.getName());
                    }
                })
                .consumer((pct, contextSupplier) -> {
                    return pct.handle(contextSupplier, new AtomicBoolean(true));
                })
                .add();
    }

    public void initialize() {
        CHANNEL = NetworkRegistry.newSimpleChannel(IdBuilder.mod(ModConstants.CHANNEL_ID),
                () -> ModConstants.NETWORK_PROTOCOL_VERSION,
                ModConstants.NETWORK_PROTOCOL_VERSION::equals,
                ModConstants.NETWORK_PROTOCOL_VERSION::equals);
        this.registerPackets();
    }

    private void registerPackets() {
        this.registerPacket(SendChatMessagePacket.class);
        this.registerPacket(ReduceChaosPacket.class);
        this.registerPacket(ChunkChaosUpdatePacket.class);
        this.registerPacket(OpenBookScreenPacket.class);
        this.registerPacket(MassChunkUpdatePacket.class);
        this.registerPacket(IncreaseChaosPacket.class);
    }

}
