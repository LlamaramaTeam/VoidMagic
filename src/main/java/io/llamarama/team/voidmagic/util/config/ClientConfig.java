package io.llamarama.team.voidmagic.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec.BooleanValue ENABLE_PEDESTAK_ITEM_RENDERING;
    public static final ForgeConfigSpec.Builder CLIENT = new ForgeConfigSpec.Builder();

    static {
        String enableItemsComment = "Enables or disables the items dynamically";
        ENABLE_PEDESTAK_ITEM_RENDERING = CLIENT.comment(enableItemsComment).define("enable_items", true);
        CLIENT.pop();
    }

}
