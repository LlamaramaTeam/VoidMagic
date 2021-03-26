package io.llamarama.team.voidmagic.datagen.provider.language;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.register.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLanguageProvider extends LanguageProvider {

    public EnglishLanguageProvider(DataGenerator gen, String locale) {
        super(gen, VoidMagic.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.voidmagic.group", "Void Magic");
        this.addBlock(ModBlocks.PUTILIAM, "Putiliam");

        VoidMagic.getLogger().info("Added english translations.");
    }

}
