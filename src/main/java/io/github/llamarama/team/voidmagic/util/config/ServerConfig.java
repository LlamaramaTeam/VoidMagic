package io.github.llamarama.team.voidmagic.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

    public static final ForgeConfigSpec INSTANCE;

    public static final ForgeConfigSpec.BooleanValue GIVE_BOOK_ON_START;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment(" Miscellanious configuration.").push("misc");

        String giveBookOnSpawnComment = " Determines whether a player will get the guide book when he first joins";
        GIVE_BOOK_ON_START = builder.comment(giveBookOnSpawnComment).define("giveBookOnStart", () -> false);
        builder.pop();

        INSTANCE = builder.build();
    }
}
