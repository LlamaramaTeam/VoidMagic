package io.github.llamarama.team.voidmagic.client.event;

import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientGameplayEventHandler {

    private static ClientGameplayEventHandler instance;

    public static ClientGameplayEventHandler getInstance() {
        if (instance == null) {
            instance = new ClientGameplayEventHandler();
        }

        return instance;
    }

    @SubscribeEvent
    public void clientTick(final TickEvent.RenderTickEvent event) {
        ClientPlayerEntity player = VoidMagicClient.getGame().player;

        if (player != null && player.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.GUIDE_BOOK.get()) {
            BlockPos position = player.getPosition();
            final AtomicInteger integer = new AtomicInteger(0);
            LazyOptional<IChaosHandler> capability =
                    player.getEntityWorld().getChunkAt(position).getCapability(VoidMagicCapabilities.CHAOS);
            capability.ifPresent((chaosHandler) -> integer.set(chaosHandler.getChaos()));

            player.sendStatusMessage(new StringTextComponent(Integer.toString(integer.get())), true);
        }
    }

    public void registerHandlers(IEventBus forgebus) {
        forgebus.register(this);
    }

}
