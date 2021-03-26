package io.llamarama.team.voidmagic.datagen.provider.model.item;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.register.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, VoidMagic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.registerNormalItem(ModItems.PUTILIAM);
        VoidMagic.getLogger().info("Added all default item models.");
    }

    private <I extends Item> void registerNormalItem(RegistryObject<I> item) {
        String path = item.getId().getPath();
        ModelFile model = new ModelFile.ExistingModelFile(mcLoc("item/generated"), this.existingFileHelper);
        this.getBuilder(path).parent(model).texture("layer0", modLoc("item/" + path));
    }

}
