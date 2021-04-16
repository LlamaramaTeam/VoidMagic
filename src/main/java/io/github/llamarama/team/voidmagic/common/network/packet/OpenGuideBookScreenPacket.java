package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.client.guide.InitialGuideBookScreen;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenGuideBookScreenPacket extends GenericPacket {

    private final int index;

    public OpenGuideBookScreenPacket(int index) {
        super();
        this.index = index;
    }

    public OpenGuideBookScreenPacket(PacketBuffer buffer) {
        super(buffer);
        this.index = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.index);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientPlayerEntity player = VoidMagicClient.getGame().player;
            if (player != null) {
                VoidMagicClient.getGame().displayGuiScreen(new InitialGuideBookScreen(new ItemStack(Items.EMERALD)));
            }
        });

        return true;
    }

}
