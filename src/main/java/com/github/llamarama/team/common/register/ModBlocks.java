package com.github.llamarama.team.common.register;

import com.github.llamarama.team.api.block.properties.ModBlockProperties;
import com.github.llamarama.team.common.block.ChalkBlock;
import com.github.llamarama.team.common.util.IdBuilder;
import com.github.llamarama.team.common.util.ModItemGroup;
import com.github.llamarama.team.common.util.misc.SettingsSupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class ModBlocks {

    private static final Map<String, Block> REGISTRY = new ConcurrentHashMap<>();

    // Some settings to be accessed quickly.
    private static final Function<Block, AbstractBlock.Settings> COPY = AbstractBlock.Settings::copy;
    private static final SettingsSupplier CHALK_PROPS = () ->
            AbstractBlock.Settings.of(Material.STONE, MapColor.LIGHT_GRAY)
                    .noCollision()
                    .nonOpaque()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.STONE)
                    .luminance((state) -> state.get(ModBlockProperties.CHALK_TYPE).getLightLevel());


    // Register Blocks
    public static final Block CHALK = register("chalk", new ChalkBlock(CHALK_PROPS.get()));

    private ModBlocks() {
    }

    @NotNull
    private static Block register(String id, Block block) {
        registerNoItem(id, block);
        Registry.register(Registry.ITEM, IdBuilder.of(id), new BlockItem(block,
                new Item.Settings().group(ModItemGroup.get())));
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
