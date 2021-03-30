package io.llamarama.team.voidmagic.datagen.provider.assets;

import com.google.common.collect.Sets;
import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import io.llamarama.team.voidmagic.util.IdBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Set;

public class ModBlockProvider extends BlockStateProvider {

    private final Set<? extends Block> blacklist;

    public ModBlockProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, VoidMagic.MODID, exFileHelper);
        this.blacklist = Sets.newHashSet();
    }

    @Override
    protected void registerStatesAndModels() {
        ModRegistries.BLOCKS.getEntries().stream()
                .filter((block) -> !this.blacklist.contains(block.get()))
                .forEach(this::simpleBlockAndItem);

        this.blacklist.forEach((block) -> {

        });

        VoidMagic.getLogger().info("Added all default block models.");
    }

    private <B extends Block> void simpleBlockAndItem(RegistryObject<B> block) {
        B actualBlock = block.get();
        if (actualBlock instanceof SlabBlock) {
            ResourceLocation blockLoc = IdBuilder.mod("block/" + block.getId().getPath().replaceAll("_slab", ""));
            this.slabBlock((SlabBlock) actualBlock, blockLoc, blockLoc);
        } else {
            this.simpleBlock(actualBlock);
            this.simpleBlockItem(actualBlock, this.cubeAll(actualBlock));
        }
    }

}
