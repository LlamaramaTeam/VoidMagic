package io.github.llamarama.team.voidmagic;

import io.github.llamarama.team.voidmagic.common.event.EventHandler;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.register.ModRegistries;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VoidMagic implements ModInitializer {

    public static final String MOD_ID = "voidmagic";
    private static final Logger LOGGER = LogManager.getLogger("VoidMagic");

    // Please don't System.out. Use this instead, it's just better in every way.
    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {

        // Old code
        /*

        // TODO Look into fabric cloth config api.
        ConfigInitializer.init(ModLoadingContext.get());


        // Initialize all Deferred Registries of the mod.
        ModRegistries.initRegistries(modBus);

        // Not needed since fabric initializes client and common separetaly.
        // Initialize the client side of the mod.
        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> VoidMagicClient::new);
         */

        ModRegistries.init();
        ModNetworking.get().init();
        EventHandler.register();
    }

}
