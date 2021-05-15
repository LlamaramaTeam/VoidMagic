package io.github.llamarama.team.voidmagic.client;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.client.event.ClientGameplayEventHandler;
import io.github.llamarama.team.voidmagic.client.event.ClientInitEventHandler;
import io.github.llamarama.team.voidmagic.client.render.multiblock.MultiblockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class VoidMagicClient {

    private static final Minecraft game = Minecraft.getInstance();

    public VoidMagicClient() {
        // Use these.
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Initialize all client event handlers.
        ClientInitEventHandler.getInstance().registerHandlers(modEventBus);
        ClientGameplayEventHandler.getInstance().registerHandlers(forgeBus);
        forgeBus.addListener(MultiblockRenderer::addCustomRenders);

        VoidMagic.getLogger().info("Loaded client side of VoidMagic");
    }

    // I don't know why but you should use this.
    public static Minecraft getGame() {
        return game;
    }

}
