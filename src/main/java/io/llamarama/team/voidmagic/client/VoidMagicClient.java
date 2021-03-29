package io.llamarama.team.voidmagic.client;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class VoidMagicClient {

    public VoidMagicClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

}
