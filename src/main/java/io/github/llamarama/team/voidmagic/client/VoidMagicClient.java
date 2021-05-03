package io.github.llamarama.team.voidmagic.client;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.client.event.ClientGameplayEventHandler;
import io.github.llamarama.team.voidmagic.client.event.ClientLifecycleEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class VoidMagicClient {

    private static final Minecraft GAME = Minecraft.getInstance();

    public VoidMagicClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Initialize all client event handlers.
        ClientLifecycleEventHandler.getInstance().registerHandlers(modEventBus);
        ClientGameplayEventHandler.getInstance().registerHandlers(forgeBus);

        VoidMagic.getLogger().info("Loaded client side of VoidMagic");
    }

    public static Minecraft getGame() {
        return GAME;
    }

}
