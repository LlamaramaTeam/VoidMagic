package io.llamarama.team.voidmagic.datagen;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.datagen.provider.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = VoidMagic.MODID)
public class ModDataGeneration {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(new ModLanguageProvider(gen, "en_us"));
        gen.addProvider(new ModLanguageProvider(gen, "en_gb"));
        gen.addProvider(new ModLanguageProvider(gen, "en_au"));
        gen.addProvider(new ModLanguageProvider(gen, "el_gr"));
        gen.addProvider(new ModLanguageProvider(gen, "en_ca"));
        gen.addProvider(new ModLanguageProvider(gen, "en_nz"));
        gen.addProvider(new ModLanguageProvider(gen, "en_za"));

        VoidMagic.getLogger().info("Successfully run data generation task.");
    }

}
