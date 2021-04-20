package io.github.llamarama.team.voidmagic.common.capability;

import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import io.github.llamarama.team.voidmagic.common.capability.impl.ChaosHandler;
import io.github.llamarama.team.voidmagic.common.capability.storage.ChaosStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class VoidMagicCaps {

    @CapabilityInject(IChaosHandler.class)
    public static Capability<IChaosHandler> CHAOS = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IChaosHandler.class, new ChaosStorage(), ChaosHandler::new);
    }


}
