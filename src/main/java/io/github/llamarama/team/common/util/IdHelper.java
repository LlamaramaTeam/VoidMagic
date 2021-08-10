package io.github.llamarama.team.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class IdHelper {

    @NotNull
    public static String getIdString(Block block) {
        Identifier id = Registry.BLOCK.getId(block);
        return id.toString();
    }

    @NotNull
    public static String getIdString(Item item) {
        Identifier id = Registry.ITEM.getId(item);
        return id.toString();
    }

}
