package io.github.llamarama.team.voidmagic.common.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static final ForgeConfigSpec INSTANCE;

    public static final ForgeConfigSpec.BooleanValue CURIOS_ENABLED;
    public static final ForgeConfigSpec.BooleanValue JEI_ENABLED;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment(" Used to enable/disable integration with other mods.").push("integration");

        String curiosComment = " Enable of disable curios support.(Requires minecraft restart)";
        CURIOS_ENABLED = builder.push("curios").comment(curiosComment).define("enableCurios", () -> true);
        builder.pop();

        String jeiComment = " Enables/disables integration with JEI.(Required minecraft restart.)";
        JEI_ENABLED = builder.push("jei").comment(jeiComment).define("enableJei", () -> true);
        builder.pop();
        builder.pop();

        INSTANCE = builder.build();
    }
}
