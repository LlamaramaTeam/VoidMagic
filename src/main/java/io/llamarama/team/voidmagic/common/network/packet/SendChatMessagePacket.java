package io.llamarama.team.voidmagic.common.network.packet;

import io.llamarama.team.voidmagic.client.VoidMagicClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendChatMessagePacket implements IPacket {

    private final String message;

    public SendChatMessagePacket(String message) {
        this.message = message;
    }

    public static SendChatMessagePacket decode(PacketBuffer buffer) {
        int length = buffer.readInt();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(buffer.readChar());
        }

        return new SendChatMessagePacket(builder.toString());
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.message.length());
        this.message.chars().forEach(buffer::writeChar);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientPlayerEntity player = VoidMagicClient.getGame().player;
            if (player != null) {
                player.sendChatMessage(this.message);
            }
        });

        return true;
    }

}
