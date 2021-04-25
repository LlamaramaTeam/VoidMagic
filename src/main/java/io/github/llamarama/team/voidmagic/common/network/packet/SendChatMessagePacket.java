package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class SendChatMessagePacket extends GenericPacket {

    private final String message;

    public SendChatMessagePacket(String message) {
        super();
        this.message = message;
    }

    public SendChatMessagePacket(PacketBuffer buffer) {
        super(buffer);
        int length = buffer.readInt();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(buffer.readChar());
        }

        this.message = builder.toString();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.message.length());
        this.message.chars().forEach(buffer::writeChar);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier, AtomicBoolean result) {
        contextSupplier.get().enqueueWork(() -> {
            ClientPlayerEntity player = VoidMagicClient.getGame().player;
            if (player != null) {
                player.sendChatMessage(this.message);
            }
        });
        contextSupplier.get().setPacketHandled(result.get());
        return result.get();
    }

}
