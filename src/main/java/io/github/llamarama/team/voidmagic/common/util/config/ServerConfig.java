package io.github.llamarama.team.voidmagic.common.util.config;

import com.google.common.collect.Lists;
import io.github.llamarama.team.voidmagic.common.util.IdHelper;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {

    public static final ForgeConfigSpec INSTANCE;

    public static final ForgeConfigSpec.BooleanValue GIVE_BOOK_ON_START;
    public static final ForgeConfigSpec.IntValue MIN_CHAOS;
    public static final ForgeConfigSpec.IntValue MAX_CHAOS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SPELLBINDING_CLOTH_BLACKLIST;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment(" Miscellanious configuration.").push("misc");

        String giveBookOnSpawnComment = " Determines whether a player will get the guide book when he first joins";
        GIVE_BOOK_ON_START = builder.comment(giveBookOnSpawnComment).define("giveBookOnStart", () -> false);

        String commentBlacklist = "The blocks that are included in this list will be ignored by the Spellbinding " +
                "Cloth item";
        ArrayList<String> defaultVals = Lists.newArrayList(IdHelper.getFullIdString(Blocks.SPAWNER),
                IdHelper.getFullIdString(Blocks.COMMAND_BLOCK),
                IdHelper.getFullIdString(Blocks.REPEATING_COMMAND_BLOCK));
        SPELLBINDING_CLOTH_BLACKLIST = builder.comment(commentBlacklist)
                .defineList("clothBlacklist",
                        defaultVals,
                        (object) -> object instanceof String &&
                                IdHelper.getBlockFromID((String) object).isPresent());
        builder.pop();

        builder.comment("Settings that manage chaos.").push("chaosSettings");

        String minValueComment = "The minimum value of chaos a chunk can have.";
        MIN_CHAOS = builder.comment(minValueComment).defineInRange("minChaos", 0, 0, 1000);

        String maxValueComment = "The maximum value of chaos a chunk can have.";
        MAX_CHAOS = builder.comment(maxValueComment).defineInRange("maxChaos", 1000, 1000, 10000);


        builder.pop();
        INSTANCE = builder.build();
    }
}
