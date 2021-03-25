package io.llamarama.team.voidmagic.datagen.provider.language;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class GreekLanguageProvider extends LanguageProvider {

    public GreekLanguageProvider(DataGenerator gen, String locale) {
        super(gen, VoidMagic.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.voidmagic.group", "Μαγεία Κενού");

        VoidMagic.getLogger().info("Added the greek translation.");
    }

}
