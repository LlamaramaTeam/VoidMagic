package io.github.llamarama.team.voidmagic.datagen.provider.assets;

import com.google.common.collect.Sets;
import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.common.register.ModRegistries;
import io.github.llamarama.team.voidmagic.util.IdBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Set;

public class ModItemModelProvider extends ItemModelProvider {

    private final Set<Item> blacklist;

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, VoidMagic.MOD_ID, existingFileHelper);
        this.blacklist = Sets.newHashSet();
    }

    @Override
    protected void registerModels() {
        VoidMagic.getLogger().info("Starting item providers");
        this.blacklist.add(ModItems.GUIDE_BOOK.get());

        ModRegistries.ITEMS.getEntries().stream()
                .filter((registryObject) -> !this.blacklist.contains(registryObject.get()))
                .filter((registryObject) -> !(registryObject.get() instanceof BlockItem))
                .forEach(this::registerNormalItem);

        this.blacklist.forEach((item) -> {

        });

        VoidMagic.getLogger().info("Added all default item models.");
    }

    private <I extends Item> void registerNormalItem(RegistryObject<I> item) {
        String path = item.getId().getPath();
        ModelFile model = new ModelFile.ExistingModelFile(mcLoc("item/generated"), this.existingFileHelper);
        this.getBuilder(path).parent(model).texture("layer0", IdBuilder.mod("item/" + path));
    }

}
