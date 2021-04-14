package io.llamarama.team.voidmagic.util.config;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigInitializer {

    public static void init(ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.INSTANCE);
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.INSTANCE);
        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.INSTANCE);
    }

}
