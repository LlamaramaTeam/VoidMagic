package io.llamarama.team.voidmagic.common.event;

import io.llamarama.team.voidmagic.common.integration.CuriosIntegration;
import io.llamarama.team.voidmagic.common.network.ModNetworking;
import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraftforge.eventbus.api.IEventBus;
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

    public void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(StringConstants.CURIOS_ID.get())) {
            CuriosIntegration.getInstance().enableSupport(event);
        }
    }

    public void processIMC(final InterModProcessEvent event) {

    }

    public void commotInit(final FMLCommonSetupEvent event) {
        ModNetworking.get().initialize();
    }

    public void registerHandlers(final IEventBus modBus) {
        modBus.addListener(this::commotInit);
        modBus.addListener(this::enqueueIMC);
        modBus.addListener(this::processIMC);
    }

}
