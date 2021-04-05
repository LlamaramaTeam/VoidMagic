package io.llamarama.team.voidmagic.datagen.provider.data;

import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.tag.ModTags;
import io.llamarama.team.voidmagic.util.IdBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.function.Consumer;

public class ModRecipeProvider extends ForgeRecipeProvider {

    private final String HAS_ITEM;
    private Consumer<IFinishedRecipe> consumer;

    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
        this.HAS_ITEM = "has_item";
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        this.consumer = consumer;
        // Shaped Recipes
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.CHISELED_WITHERED_STONE_BRICKS.get(), 1)
                .key('#', ModBlocks.WITHERED_STONE_BRICK_SLAB.get())
                .patternLine("#")
                .patternLine("#")
                .addCriterion(this.HAS_ITEM, RecipeProvider.hasItem(ModBlocks.WITHERED_STONE_BRICKS.get()))
                .build(consumer);
        this.createBrickRecipe(ModBlocks.WITHERED_STONE_BRICKS.get(), ModBlocks.WITHERED_STONE.get());
        this.createBrickRecipe(ModBlocks.TOFAL.get(), ModBlocks.TOFAL_BRICKS.get());
        this.createSlabRecipe(ModBlocks.WITHERED_STONE_SLAB.get(), ModBlocks.WITHERED_STONE.get());
        this.createSlabRecipe(ModBlocks.WITHERED_STONE_BRICK_SLAB.get(), ModBlocks.WITHERED_STONE_BRICKS.get());
        this.createSlabRecipe(ModBlocks.TOFAL_BRICKS_SLAB.get(), ModBlocks.TOFAL_BRICKS.get());
        this.createSlabRecipe(ModBlocks.TOFAL_TILES_SLAB.get(), ModBlocks.TOFAL_TILES.get());
        this.createStairsRecipe(ModBlocks.WITHERED_STONE_BRICKS.get(), ModBlocks.WITHERED_STONE_BRICK_STAIRS.get());
        this.createStairsRecipe(ModBlocks.TOFAL_BRICKS.get(), ModBlocks.TOFAL_BRICK_STAIRS.get());

        // Smelting Recipes
        CookingRecipeBuilder.smeltingRecipe(
                Ingredient.fromItems(ModBlocks.WITHERED_STONE_BRICKS.get()),
                ModBlocks.CRACKED_WITHER_STONE_BRICKS.get(), 0.2f, 200)
                .addCriterion(this.HAS_ITEM, RecipeProvider.hasItem(ModBlocks.WITHERED_STONE_BRICKS.get()))
                .build(consumer);

        // Stonecutter
        this.createCuttingRecipeFromTag(ModTags.Items.WITHERED_STONE_BLOCKS, ModBlocks.WITHERED_STONE.get());
        this.createCuttingRecipeFromTag(ModTags.Items.WITHERED_STONE_BLOCKS, ModBlocks.WITHERED_STONE_BRICKS.get());
        this.createCuttingRecipeFromTag(ModTags.Items.WITHERED_STONE_BLOCKS, ModBlocks.CHISELED_WITHERED_STONE_BRICKS.get());
        this.createCuttingRecipeFromTag(ModTags.Items.TOFAL_BLOCKS, ModBlocks.TOFAL.get());
        this.createCuttingRecipeFromTag(ModTags.Items.TOFAL_BLOCKS, ModBlocks.TOFAL_BRICKS.get());
        this.createCuttingRecipeFromTag(ModTags.Items.TOFAL_BLOCKS, ModBlocks.TOFAL_TILES.get());

        this.createCuttingRecipeFromTag(ModTags.Items.WITHERED_STONE_BLOCKS, ModBlocks.WITHERED_STONE_BRICK_SLAB.get());
        this.createCuttingRecipeFromTag(ModTags.Items.WITHERED_STONE_BLOCKS, ModBlocks.WITHERED_STONE_SLAB.get());
        this.createCuttingRecipeFromTag(ModTags.Items.WITHERED_STONE_BLOCKS, ModBlocks.WITHERED_STONE_PILLAR.get());
        this.createCuttingRecipeFromTag(ModTags.Items.WITHERED_STONE_BLOCKS, ModBlocks.WITHERED_STONE_BRICK_STAIRS.get());
        this.createCuttingRecipeFromTag(ModTags.Items.TOFAL_BLOCKS, ModBlocks.TOFAL_TILES_SLAB.get());
        this.createCuttingRecipeFromTag(ModTags.Items.TOFAL_BLOCKS, ModBlocks.TOFAL_BRICKS_SLAB.get());
        this.createCuttingRecipeFromTag(ModTags.Items.TOFAL_BLOCKS, ModBlocks.TOFAL_BRICK_STAIRS.get());
    }

    private void createBrickRecipe(Block brickBlock, Block original) {
        ShapedRecipeBuilder.shapedRecipe(brickBlock, 4)
                .key('#', original)
                .patternLine("##")
                .patternLine("##")
                .addCriterion(this.HAS_ITEM, RecipeProvider.hasItem(original))
                .build(this.consumer);
    }

    private void createSlabRecipe(Block slab, Block original) {
        ShapedRecipeBuilder.shapedRecipe(slab, 6)
                .key('b', original)
                .patternLine("bbb")
                .addCriterion(this.HAS_ITEM, RecipeProvider.hasItem(original))
                .build(this.consumer);
    }

    @SuppressWarnings("unused")
    private void createStoneCuttingRecipe(Block blockIn, Block blockOut) {
        int countOut = 1;

        if (blockOut instanceof SlabBlock) {
            countOut = 2;
        }

        if (blockIn.getRegistryName() == null || blockOut.getRegistryName() == null) {
            return;
        }

        SingleItemRecipeBuilder
                .stonecuttingRecipe(Ingredient.fromItems(blockIn), blockOut, countOut)
                .addCriterion(this.HAS_ITEM, RecipeProvider.hasItem(blockIn))
                .build(this.consumer, IdBuilder.mod(blockIn.getRegistryName().getPath() + "_to_" + blockOut.getRegistryName().getPath()));
    }

    private void createCuttingRecipeFromTag(ITag<Item> tag, Block block) {
        int countOut = 1;

        if (block instanceof SlabBlock) {
            countOut = 2;
        }

        if (block.getRegistryName() == null) {
            return;
        }

        SingleItemRecipeBuilder
                .stonecuttingRecipe(Ingredient.fromTag(tag), block, countOut)
                .addCriterion(this.HAS_ITEM, RecipeProvider.hasItem(Items.STONECUTTER))
                .build(this.consumer, IdBuilder.mod("stonecutting_to" + block.getRegistryName().getPath()));
    }

    private void createStairsRecipe(Block in, Block stairs) {
        ShapedRecipeBuilder.shapedRecipe(stairs, 4)
                .key('#', in)
                .patternLine("  #")
                .patternLine(" ##")
                .patternLine("###")
                .addCriterion(this.HAS_ITEM, RecipeProvider.hasItem(in))
                .build(this.consumer);
    }

}
