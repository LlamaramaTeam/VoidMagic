package io.github.llamarama.team.voidmagic.datagen;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.util.constants.ModConstants;
import io.github.llamarama.team.voidmagic.datagen.provider.assets.EnglishLanguageProvider;
import io.github.llamarama.team.voidmagic.datagen.provider.assets.ModBlockProvider;
import io.github.llamarama.team.voidmagic.datagen.provider.assets.ModItemModelProvider;
import io.github.llamarama.team.voidmagic.datagen.provider.data.ModLootTableProvider;
import io.github.llamarama.team.voidmagic.datagen.provider.data.ModRecipeProvider;
import io.github.llamarama.team.voidmagic.datagen.provider.data.tags.ModBlockTagsProvider;
import io.github.llamarama.team.voidmagic.datagen.provider.data.tags.ModItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * Register all data providers using the {@link GatherDataEvent} provided by forge.
 *
 * @author 0xJoeMama
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ModConstants.MOD_ID)
public final class ModDataGeneration {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        /*
            Assets
         */

        // Lang Providers
        // Currently only english is supported and any other language will probably have to be added specifically.
        gen.addProvider(new EnglishLanguageProvider(gen, "en_us"));
        gen.addProvider(new EnglishLanguageProvider(gen, "en_gb"));
        gen.addProvider(new EnglishLanguageProvider(gen, "en_au"));

        // Item Models
        gen.addProvider(new ModItemModelProvider(gen, helper));

        // Block Models
        gen.addProvider(new ModBlockProvider(gen, helper));

        /*
            Data
         */

        // Loot Tables
        gen.addProvider(new ModLootTableProvider(gen));

        // Tags
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(gen, helper);
        gen.addProvider(blockTagsProvider);
        gen.addProvider(new ModItemTagsProvider(gen, blockTagsProvider, helper));

        // Recipes
        gen.addProvider(new ModRecipeProvider(gen));

        VoidMagic.getLogger().info("Successfully run data generation task.");
    }

}
