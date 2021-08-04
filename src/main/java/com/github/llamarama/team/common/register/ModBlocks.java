package com.github.llamarama.team.common.register;

import com.github.llamarama.team.common.util.IdBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class ModBlocks {

    private static final Map<String, Block> REGISTRY = new ConcurrentHashMap<>();

    // Some settings to be accessed quickly.
    private static final Function<Block, AbstractBlock.Settings> COPY = AbstractBlock.Settings::copy;


    // Register Blocks

    private ModBlocks() {
    }

    @NotNull
    private static Block register(String id, Block block) {
        registerNoItem(id, block);
        Registry.register(Registry.ITEM, IdBuilder.of(id), new BlockItem(block, new Item.Settings()));
        return block;
    }

    @NotNull
    private static Block registerNoItem(String id, @NotNull Block block) {
        REGISTRY.putIfAbsent(id, block);
        return block;
    }

    static void init() {
        REGISTRY.forEach((id, block) -> Registry.register(Registry.BLOCK, IdBuilder.of(id), block));
    }

}
