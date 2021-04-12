package io.llamarama.team.voidmagic;

import io.llamarama.team.voidmagic.client.VoidMagicClient;
import io.llamarama.team.voidmagic.common.event.CommonInitEventHandler;
import io.llamarama.team.voidmagic.common.event.GameplayEventHandler;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import io.llamarama.team.voidmagic.util.config.ConfigInitializer;
import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(StringConstants.MOD_ID_STR)
public class VoidMagic {

    public static final String MOD_ID = StringConstants.MOD_ID.get();
    private static final Logger LOGGER = LogManager.getLogger("Void Magic");

    public VoidMagic() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        // Initialize the mod's configuration.
        ConfigInitializer.init(ModLoadingContext.get());

        CommonInitEventHandler.getInstance().registerHandlers(modBus);
        GameplayEventHandler.getInstance().registerHandlers(forgeBus);
        ModRegistries.initRegistries(modBus);

        // Initialize the client side of the mod.
        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> VoidMagicClient::new);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

}
