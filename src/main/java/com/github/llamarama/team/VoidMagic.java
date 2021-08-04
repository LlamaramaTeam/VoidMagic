package com.github.llamarama.team;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VoidMagic implements ModInitializer {

    private static final Logger LOGGER = LogManager.getLogger("VoidMagic");

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void log(String msg) {
        LOGGER.debug(msg);
    }

    @Override
    public void onInitialize() {

    }

}
