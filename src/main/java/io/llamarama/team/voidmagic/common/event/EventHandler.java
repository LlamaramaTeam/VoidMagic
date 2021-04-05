package io.llamarama.team.voidmagic.common.event;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public final class EventHandler {

    private static EventHandler instance;

    public void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(StringConstants.CURIOS_ID)) {
            VoidMagic.getLogger().info("Curios was loaded successfully.");
        }
    }

    public void processIMC(final InterModProcessEvent event) {

    }

    public static EventHandler getInstance() {
        if (instance == null) {
            instance = new EventHandler();
        }
        return instance;
    }

    public void startListeners(IEventBus forgeBus) {
        forgeBus.addListener(this::enqueueIMC);
        forgeBus.addListener(this::processIMC);
    }

    private EventHandler() {
    }

}
