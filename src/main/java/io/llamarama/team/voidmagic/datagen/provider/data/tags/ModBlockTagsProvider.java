package io.llamarama.team.voidmagic.datagen.provider.data.tags;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.tag.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public final class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, VoidMagic.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(ModTags.Blocks.TOFAL_BLOCKS)
                .add(ModBlocks.TOFAL.get())
                .add(ModBlocks.TOFAL_BRICKS.get())
                .add(ModBlocks.TOFAL_TILES.get());
        this.getOrCreateBuilder(ModTags.Blocks.WITHERED_STONE_BLOCKS)
                .add(ModBlocks.WITHERED_STONE.get())
                .add(ModBlocks.WITHERED_STONE_BRICKS.get())
                .add(ModBlocks.CHISELED_WITHERED_STONE_BRICKS.get());
    }


}
