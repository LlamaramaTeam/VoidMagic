package io.llamarama.team.voidmagic.common.event;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.capability.VoidMagicCapabilities;
import io.llamarama.team.voidmagic.common.integration.CuriosIntegration;
import io.llamarama.team.voidmagic.common.network.ModNetworking;
import io.llamarama.team.voidmagic.util.config.CommonConfig;
import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public final class CommonInitEventHandler {

    private static CommonInitEventHandler instance;

    private CommonInitEventHandler() {
    }

    public static CommonInitEventHandler getInstance() {
        if (instance == null) {
            instance = new CommonInitEventHandler();
        }

        return instance;
    }

    @SubscribeEvent
    public void commotInit(final FMLCommonSetupEvent event) {
        ModNetworking.get().initialize();
        VoidMagicCapabilities.register();
    }

    @SubscribeEvent
    public void enqueueIMC(final InterModEnqueueEvent event) {
        boolean curiosLoaded = ModList.get().isLoaded(StringConstants.CURIOS_ID.get());
        if (curiosLoaded && CommonConfig.CURIOS_ENABLED.get()) {
            CuriosIntegration.getInstance().enableSupport(event);
            VoidMagic.getLogger().info("Successfully added Curios integration.");
        }
    }

    @SubscribeEvent
    public void processIMC(final InterModProcessEvent event) {

    }

    public void registerHandlers(final IEventBus modBus) {
        modBus.register(this);
    }

}
