package io.github.llamarama.team.voidmagic.common.network.packet;

import io.github.llamarama.team.voidmagic.client.misc.ScreenUtilities;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class OpenBookScreenPacket extends GenericPacket {

    private final ItemStack stack;

    public OpenBookScreenPacket(ItemStack stack) {
        this.stack = stack;
    }

    public OpenBookScreenPacket(PacketBuffer buffer) {
        super(buffer);

        this.stack = buffer.readItemStack();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(stack);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        final AtomicBoolean out = new AtomicBoolean(true);

        contextSupplier.get().enqueueWork(() -> ScreenUtilities.openInitialBookScreen(this.stack));

        contextSupplier.get().setPacketHandled(out.get());
        return out.get();
    }

}
