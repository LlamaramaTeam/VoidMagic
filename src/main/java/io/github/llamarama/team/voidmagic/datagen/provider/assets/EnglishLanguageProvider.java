package io.github.llamarama.team.voidmagic.datagen.provider.assets;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.item.PackedBlockItem;
import io.github.llamarama.team.voidmagic.common.item.SpellBindingClothItem;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.common.register.ModRegistries;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class EnglishLanguageProvider extends LanguageProvider {

    private final HashSet<RegistryObject<?>> blacklist = new HashSet<>();

    public EnglishLanguageProvider(DataGenerator gen, String locale) {
        super(gen, VoidMagic.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        // Blacklist calls.
        this.blacklist.add(ModItems.PACKED_BLOCK);

        // Specific key definitions.
        this.add("itemGroup.voidmagic.group", "Void Magic");
        this.add(PackedBlockItem.CONTENT_KEY, "Contents: %s");
        this.add(PackedBlockItem.CONTAINS_KEY, "Contains %s stacks.");
        this.add(SpellBindingClothItem.SHINY_KEY, "Shinny...");
        this.add(PackedBlockItem.KEY, "Packed Block");

        ModRegistries.BLOCKS.getEntries().forEach(this::getCamelCaseBlockName);

        ModRegistries.ITEMS.getEntries().stream()
                .filter((registryObject) -> !(registryObject.get() instanceof BlockItem))
                .filter((registryObject) -> !this.blacklist.contains(registryObject))
                .forEach(this::getCamelCaseItemName);

        VoidMagic.getLogger().info("Added english translations.");
    }

    private void getCamelCaseItemName(RegistryObject<Item> item) {
        this.addItem(item, this.makeCorrectName(item));
    }

    @NotNull
    private String makeCorrectName(RegistryObject<?> item) {
        String[] name = item.getId().getPath().split("_");

        StringBuilder builder = new StringBuilder();
        String currentWord;

        for (String s : name) {
            char firstChar = s.charAt(0);
            currentWord = s.replaceFirst(String.valueOf(firstChar), String.valueOf(Character.toUpperCase(firstChar)));
            builder.append(currentWord).append(" ");
        }
        return builder.toString();
    }

    private void getCamelCaseBlockName(RegistryObject<Block> block) {
        this.addBlock(block, this.makeCorrectName(block));
    }

}
