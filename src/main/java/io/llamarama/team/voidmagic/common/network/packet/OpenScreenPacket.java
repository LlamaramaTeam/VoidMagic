package io.llamarama.team.voidmagic.common.network.packet;

import io.llamarama.team.voidmagic.client.VoidMagicClient;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class OpenScreenPacket implements IPacket {

    private final Screen screenTarget;
    private String message;

    public OpenScreenPacket(Screen screenTarget, String message) {
        this.screenTarget = screenTarget;
        this.message = message;
    }

    /**
     * Only used so register the packet.
     */
    public OpenScreenPacket() {
        this.screenTarget = null;
    }

    @Override
    public void encode(IPacket packet, PacketBuffer buffer) {
        buffer.writeInt(this.message.length());
        buffer.writeString(this.message);
    }

    @Override
    public <PCT extends IPacket> PCT decode(PacketBuffer buffer) {
        int length = buffer.readInt();
        String message = buffer.readString(length);
        return (PCT) new OpenScreenPacket(null, message);
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

    protected Screen getScreenTarget() {
        return this.screenTarget;
    }

}
