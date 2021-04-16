package io.github.llamarama.team.voidmagic.client;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.client.event.ClientInitEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class VoidMagicClient {

    private static final Minecraft game = Minecraft.getInstance();

    public VoidMagicClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ClientInitEventHandler.getInstance().registerHandlers(modEventBus);
        VoidMagic.getLogger().info("Loaded client side of VoidMagic");
    }

    public static Minecraft getGame() {
        return game;
    }

}
