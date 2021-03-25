package io.llamarama.team.voidmagic.datagen.provider;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, VoidMagic.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.voidmagic.group", "Void Magic");
    }

}
