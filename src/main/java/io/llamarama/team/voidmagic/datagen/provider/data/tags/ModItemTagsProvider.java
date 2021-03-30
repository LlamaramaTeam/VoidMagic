package io.llamarama.team.voidmagic.datagen.provider.data.tags;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.tag.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, VoidMagic.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.copy(ModTags.Blocks.TOFAL_BLOCKS, ModTags.Items.TOFAL_BLOCKS);
        this.copy(ModTags.Blocks.WITHERED_STONE_BLOCKS, ModTags.Items.WITHERED_STONE_BLOCKS);
    }

}
