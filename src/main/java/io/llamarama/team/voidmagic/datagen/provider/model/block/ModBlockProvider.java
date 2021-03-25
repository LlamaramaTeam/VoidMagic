package io.llamarama.team.voidmagic.datagen.provider.model.block;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockProvider extends BlockStateProvider {

    public ModBlockProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, VoidMagic.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        VoidMagic.getLogger().info("Added all default block models.");
    }

}
