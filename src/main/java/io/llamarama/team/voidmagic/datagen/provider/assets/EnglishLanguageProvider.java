package io.llamarama.team.voidmagic.datagen.provider.assets;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.register.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLanguageProvider extends LanguageProvider {

    public EnglishLanguageProvider(DataGenerator gen, String locale) {
        super(gen, VoidMagic.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.voidmagic.group", "Void Magic");
        this.addItem(ModItems.PUTILIAM, "Putiliam");
        this.addBlock(ModBlocks.WITHERED_STONE, "Withered Stone");
        this.addBlock(ModBlocks.WITHERED_STONE_BRICKS, "Withered Stone Bricks");
        this.addItem(ModItems.GUIDE_BOOK, "Guide Book(WIP)");

        VoidMagic.getLogger().info("Added english translations.");
    }

}
