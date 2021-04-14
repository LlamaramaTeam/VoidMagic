package io.llamarama.team.voidmagic.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec INSTANCE;

    public static final ForgeConfigSpec.BooleanValue ENABLE_ITEM_RENDERING;
    public static final ForgeConfigSpec.IntValue TICKS_PER_ROTATION;
    public static final ForgeConfigSpec.BooleanValue SPECIAL_BLOCK_RENDERING;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment(" Rendering utilities, most of the time you shouldn't change them.").push("rendering");

        String enableItemsComment = " Enables or disables the items on top of pedestals being rendered dynamically";
        ENABLE_ITEM_RENDERING = builder.comment(enableItemsComment).define("renderItems", () -> true);

        String tickPerRotComment = " Determines the amount of time a full rotation takes for an item on a pedestal.";
        TICKS_PER_ROTATION = builder.comment(tickPerRotComment).defineInRange("ticks", 360, 20, 1200);

        String specialBlockComment = " Allows certain blocks to be rendered differently on the pedestal.";
        SPECIAL_BLOCK_RENDERING = builder.comment(specialBlockComment).define("specialBlockRendering", true);


        INSTANCE = builder.build();
    }

}
