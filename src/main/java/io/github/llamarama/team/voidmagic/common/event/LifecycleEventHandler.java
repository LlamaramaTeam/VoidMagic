package io.github.llamarama.team.voidmagic.common.event;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCaps;
import io.github.llamarama.team.voidmagic.common.integration.CuriosIntegration;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.util.config.CommonConfig;
import io.github.llamarama.team.voidmagic.common.util.constants.ModConstants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public final class LifecycleEventHandler implements IEventHandler {

    private static LifecycleEventHandler instance;

    private LifecycleEventHandler() {
    }

    public static LifecycleEventHandler getInstance() {
        if (instance == null) {
            instance = new LifecycleEventHandler();
        }

        return instance;
    }

    @SubscribeEvent
    public void commotInit(final FMLCommonSetupEvent event) {
        ModNetworking.get().initialize();
        VoidMagicCaps.register();
    }

    @SubscribeEvent
    public void enqueueIMC(final InterModEnqueueEvent event) {
        boolean curiosLoaded = ModList.get().isLoaded(ModConstants.CURIOS_ID);
        if (curiosLoaded && CommonConfig.CURIOS_ENABLED.get()) {
            CuriosIntegration.getInstance().enableSupport(event);
            VoidMagic.getLogger().info("Successfully added Curios integration.");
        }
    }

    @SubscribeEvent
    public void processIMC(final InterModProcessEvent event) {

    }

}
