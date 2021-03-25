package io.llamarama.team.voidmagic.datagen.provider.language;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLanguageProvider extends LanguageProvider {

    public EnglishLanguageProvider(DataGenerator gen, String locale) {
        super(gen, VoidMagic.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.voidmagic.group", "Void Magic");

        VoidMagic.getLogger().info("Added english translations.");
    }

}
