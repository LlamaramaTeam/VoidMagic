package io.github.llamarama.team.voidmagic;

import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.event.GameplayEventHandler;
import io.github.llamarama.team.voidmagic.common.event.LifecycleEventHandler;
import io.github.llamarama.team.voidmagic.common.event.WorldgenEventHandler;
import io.github.llamarama.team.voidmagic.common.register.ModRegistries;
import io.github.llamarama.team.voidmagic.common.util.config.ConfigInitializer;
import io.github.llamarama.team.voidmagic.common.util.constants.ModConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hallo.
 *
 * @author 0xJoeMama
 * @since 2021
 */
@Mod(ModConstants.MOD_ID)
public class VoidMagic {

    public static final String MOD_ID = ModConstants.MOD_ID;
    private static final Logger LOGGER = LogManager.getLogger("VoidMagic");

    public VoidMagic() {
        // Use these.
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Initialize the mod's configuration.
        ConfigInitializer.init(ModLoadingContext.get());

        // Initialize all Deferred Registries of the mod.
        ModRegistries.initRegistries(modBus);

        // Register all of the mods event handlers.
        LifecycleEventHandler.getInstance().registerHandlers(modBus);
        GameplayEventHandler.getInstance().registerHandlers(forgeBus);
        WorldgenEventHandler.getInstance().registerHandlers(forgeBus);


        // Initialize the client side of the mod.
        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> VoidMagicClient::new);
    }

    // Please don't System.out. Use this instead it's just better in every single way.
    public static Logger getLogger() {
        return LOGGER;
    }

}
