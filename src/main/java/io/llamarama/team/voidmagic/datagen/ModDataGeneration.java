package io.llamarama.team.voidmagic.datagen;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.datagen.provider.language.EnglishLanguageProvider;
import io.llamarama.team.voidmagic.datagen.provider.language.GreekLanguageProvider;
import io.llamarama.team.voidmagic.datagen.provider.model.block.ModBlockProvider;
import io.llamarama.team.voidmagic.datagen.provider.model.item.ModItemModelProvider;
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
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = VoidMagic.MODID)
public final class ModDataGeneration {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // Lang Providers
        gen.addProvider(new EnglishLanguageProvider(gen, "en_us"));
        gen.addProvider(new EnglishLanguageProvider(gen, "en_gb"));
        gen.addProvider(new EnglishLanguageProvider(gen, "en_au"));
        gen.addProvider(new EnglishLanguageProvider(gen, "en_ca"));
        gen.addProvider(new EnglishLanguageProvider(gen, "en_nz"));
        gen.addProvider(new EnglishLanguageProvider(gen, "en_za"));

        // Greek Translation
        gen.addProvider(new GreekLanguageProvider(gen, "el_gr"));

        // Item Models
        gen.addProvider(new ModItemModelProvider(gen, helper));

        // Block Models
        gen.addProvider(new ModBlockProvider(gen, helper));

        VoidMagic.getLogger().info("Successfully run data generation task.");
    }

}
