package io.llamarama.team.voidmagic.common.integration;

import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public interface IIntegrator {

    void enableSupport(final InterModEnqueueEvent event);

}
