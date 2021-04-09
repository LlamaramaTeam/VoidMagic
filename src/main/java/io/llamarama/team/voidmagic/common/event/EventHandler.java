package io.llamarama.team.voidmagic.common.event;

import io.llamarama.team.voidmagic.common.integration.CuriosIntegration;
import io.llamarama.team.voidmagic.common.network.ModNetworking;
import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@Mod.EventBusSubscriber(modid = StringConstants.MOD_ID_STR, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EventHandler {

    private EventHandler() {
    }

    @SubscribeEvent
    public static void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(StringConstants.CURIOS_ID.get())) {
            CuriosIntegration.getInstance().enableSupport(event);
        }
    }

    @SubscribeEvent
    public static void processIMC(final InterModProcessEvent event) {

    }

    @SubscribeEvent
    public static void onCommonInit(final FMLCommonSetupEvent event) {
        ModNetworking.get().initialize();
    }

}
