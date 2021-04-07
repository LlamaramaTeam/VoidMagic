package io.llamarama.team.voidmagic.common.integration;

import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public class CuriosIntegration implements IIntegrator {

    private static CuriosIntegration instance;

    public static CuriosIntegration getInstance() {
        if (instance == null) {
            instance = new CuriosIntegration();
        }

        return instance;
    }

    @Override
    public void enableSupport(final InterModEnqueueEvent event) {

    }

}
