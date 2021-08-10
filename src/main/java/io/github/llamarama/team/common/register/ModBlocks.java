package io.github.llamarama.team.common.register;

import io.github.llamarama.team.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.common.block.PillarBlock;
import io.github.llamarama.team.common.block.*;
import io.github.llamarama.team.common.util.IdBuilder;
import io.github.llamarama.team.common.util.ModItemGroup;
import io.github.llamarama.team.common.util.misc.SettingsSupplier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@SuppressWarnings("unused")
public final class ModBlocks {

    private static final Map<String, Block> REGISTRY = new ConcurrentHashMap<>();

    // Some settings to be accessed quickly.
    private static final Function<Block, AbstractBlock.Settings> COPY = AbstractBlock.Settings::copy;
    private static final SettingsSupplier CHALK_PROPS = () ->
            FabricBlockSettings.of(Material.STONE, MapColor.LIGHT_GRAY)
                    .noCollision()
                    .nonOpaque()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.STONE)
                    .luminance((state) -> state.get(ModBlockProperties.CHALK_TYPE).getLightLevel());
    // Register Blocks
    public static final Block CHALK = register("chalk",
            new ChalkBlock(CHALK_PROPS.get()));
    private static final SettingsSupplier WITHERED_STONE_PROPS = () ->
            FabricBlockSettings.of(Material.STONE, MapColor.GRAY)
                    .requiresTool()
                    .strength(3.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 3);

    public static final Block WITHERED_STONE = register("withered_stone",
            new Block(WITHERED_STONE_PROPS.get()));
    public static final Block POLISHED_WITHERED_STONE = register("polished_withered_stone",
            new Block(COPY.apply(WITHERED_STONE)));
    public static final Block CRACKED_WITHERED_STONE_BRICKS = register("cracked_withered_stone_bricks",
            new Block(COPY.apply(WITHERED_STONE)));
    public static final Block CHISELED_WITHERED_STONE_BRICKS = register("chiseled_withered_stone_bricks",
            new Block(COPY.apply(WITHERED_STONE)));
    public static final Block WITHERED_STONE_PILLAR = register("withered_stone_pillar",
            new PillarBlock(COPY.apply(WITHERED_STONE)));
    public static final Block WITHERED_STONE_PLATE = register("withered_stone_plate",
            new PlateBlock(COPY.apply(WITHERED_STONE)));
    public static final Block WITHERED_STONE_BRICKS = register("withered_stone_bricks",
            new Block(WITHERED_STONE_PROPS.get()));
    public static final Block WITHERED_STONE_BRICK_STAIRS = register("withered_stone_brick_stairs",
            new AccessibleStairsBlock(WITHERED_STONE_BRICKS.getDefaultState(), COPY.apply(WITHERED_STONE)));
    public static final Block WITHERED_STONE_SLAB = register("withered_stone_slab",
            new SlabBlock(WITHERED_STONE_PROPS.get()));
    public static final Block WITHERED_STONE_BRICK_SLAB = register("withered_stone_brick_slab",
            new SlabBlock(WITHERED_STONE_PROPS.get()));
    public static final Block OFFERING_PLATE = register("offering_plate",
            new OfferingPlateBlock(COPY.apply(WITHERED_STONE)));
    public static final Block DECORATIVE_PACKED_BLOCK = register("decorative_packed_block",
            new Block(COPY.apply(Blocks.WHITE_WOOL)));


    private ModBlocks() {
    }

    @NotNull
    private static Block register(String id, Block block) {
        Registry.register(Registry.ITEM, IdBuilder.of(id), new BlockItem(block,
                new Item.Settings().group(ModItemGroup.get())));
        return registerNoItem(id, block);
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
