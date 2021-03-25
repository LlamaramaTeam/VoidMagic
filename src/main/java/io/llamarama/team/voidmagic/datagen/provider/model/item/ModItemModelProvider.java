package io.llamarama.team.voidmagic.datagen.provider.model.item;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, VoidMagic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ResourceLocation generated = modLoc("item/generated");


        VoidMagic.getLogger().info("Added all default item models.");
    }

}
