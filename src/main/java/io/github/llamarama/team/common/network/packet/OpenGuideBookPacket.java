package io.github.llamarama.team.common.network.packet;

import io.github.llamarama.team.common.util.IdBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenGuideBookPacket extends DefaultS2CPacket {

    public static final Identifier OPEN_BOOK_PACKET_ID = IdBuilder.of("open_book");

    public OpenGuideBookPacket(ItemStack bookStack) {
        super(OPEN_BOOK_PACKET_ID, PacketByteBufs.create());
        PacketByteBuf buffer = this.getBuffer();

        buffer.writeItemStack(bookStack);
    }

    public OpenGuideBookPacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        super(OPEN_BOOK_PACKET_ID, client, handler, buf, responseSender);

        ItemStack bookStack = buf.readItemStack();

        client.player.sendChatMessage(bookStack.toString());
    }

}
