package io.github.llamarama.team.voidmagic;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VoidMagic implements ModInitializer {

    public static final String MOD_ID = "voidmagic";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info(MOD_ID + " is now loaded.");
    }

}
