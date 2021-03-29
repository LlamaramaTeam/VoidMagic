package io.llamarama.team.voidmagic.datagen.provider.assets;

import com.google.common.collect.Sets;
import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
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
        ModRegistries.BLOCKS.getEntries().stream().map(RegistryObject::get).filter((block) -> !this.blacklist.contains(block)).forEach(this::simpleBlockAndItem);

        this.blacklist.forEach((block) -> {

        });

        VoidMagic.getLogger().info("Added all default block models.");
    }

    private <B extends Block> void simpleBlockAndItem(B block) {
        this.simpleBlock(block);
        this.simpleBlockItem(block, this.cubeAll(block));
    }

}
