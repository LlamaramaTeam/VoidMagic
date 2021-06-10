package io.github.llamarama.team.voidmagic.client;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.client.event.ClientGameplayEventHandler;
import io.github.llamarama.team.voidmagic.client.event.ClientLifecycleEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class VoidMagicClient {

    private static final Minecraft GAME = Minecraft.getInstance();

    public VoidMagicClient() {
        // Use these.
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Initialize all client event handlers.
        ClientLifecycleEventHandler.getInstance().registerHandlers(modEventBus);
        ClientGameplayEventHandler.getInstance().registerHandlers(forgeBus);

        VoidMagic.getLogger().info("Loaded client side of VoidMagic");
    }

    // I don't know why but you should use this.
    public static Minecraft getGame() {
        return GAME;
    }

}
