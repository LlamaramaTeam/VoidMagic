package io.github.llamarama.team.voidmagic.common.event;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class WorldgenEventHandler {

    private static WorldgenEventHandler instance;

    public static WorldgenEventHandler getInstance() {
        if (instance == null) {
            instance = new WorldgenEventHandler();
        }

        return instance;
    }

    @SubscribeEvent
    public void addOres(final BiomeLoadingEvent event) {

    }

    public void registerHandlers(IEventBus forgeBus) {
        forgeBus.register(this);
    }

}
