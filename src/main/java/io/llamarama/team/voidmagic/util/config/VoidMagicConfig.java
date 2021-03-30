package io.llamarama.team.voidmagic.util.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;

public class VoidMagicConfig extends ModConfig {

    public VoidMagicConfig(Type type, ForgeConfigSpec spec, ModContainer container, String fileName) {
        super(type, spec, container, fileName);
    }

    public VoidMagicConfig(Type type, ForgeConfigSpec spec, ModContainer activeContainer) {
        super(type, spec, activeContainer);
    }

}
