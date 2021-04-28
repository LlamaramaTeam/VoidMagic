package io.github.llamarama.team.voidmagic;

import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.event.CommonInitEventHandler;
import io.github.llamarama.team.voidmagic.common.event.GameplayEventHandler;
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

@Mod(ModConstants.MOD_ID)
public class VoidMagic {

    public static final String MOD_ID = ModConstants.MOD_ID;
    private static final Logger LOGGER = LogManager.getLogger("VoidMagic");

    public VoidMagic() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Initialize the mod's configuration.
        ConfigInitializer.init(ModLoadingContext.get());

        CommonInitEventHandler.getInstance().registerHandlers(modBus);
        GameplayEventHandler.getInstance().registerHandlers(forgeBus);
        WorldgenEventHandler.getInstance().registerHandlers(forgeBus);
        ModRegistries.initRegistries(modBus);

        // Initialize the client side of the mod.
        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> VoidMagicClient::new);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

}
