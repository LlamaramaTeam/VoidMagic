package io.github.llamarama.team.voidmagic.common.event;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IEventHandler {

    default void registerHandlers(final IEventBus bus) {
        bus.register(this);
    }

}
