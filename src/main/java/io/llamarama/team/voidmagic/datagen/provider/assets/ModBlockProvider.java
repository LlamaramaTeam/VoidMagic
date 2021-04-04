package io.llamarama.team.voidmagic.datagen.provider.assets;

import com.google.common.collect.Sets;
import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.block.PillarBlock;
import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import io.llamarama.team.voidmagic.util.IdBuilder;
import io.llamarama.team.voidmagic.util.IdHelper;
import io.llamarama.team.voidmagic.util.VectorHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Set;

public class ModBlockProvider extends BlockStateProvider {

    private final Set<Block> blacklist;

    public ModBlockProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, VoidMagic.MODID, exFileHelper);
        this.blacklist = Sets.newHashSet();
    }

    @Override
    protected void registerStatesAndModels() {
        this.blacklist.add(ModBlocks.WITHERED_STONE_PILLAR.get());

        ModRegistries.BLOCKS.getEntries().stream()
                .filter((block) -> !this.blacklist.contains(block.get()))
                .forEach(this::simpleBlockAndItem);

        this.blacklist.forEach((block) -> {
            if (block instanceof PillarBlock) {
                this.pillarBlockAndItem(block);
            }
        });

        VoidMagic.getLogger().info("Added all default block models.");
    }

    private <B extends Block> void simpleBlockAndItem(RegistryObject<B> block) {
        B actualBlock = block.get();
        if (actualBlock instanceof SlabBlock) {
            ResourceLocation blockLoc =
                    IdBuilder.mod("block/" + block.getId().getPath().replaceAll("_slab", ""));
            ResourceLocation itemLoc = IdBuilder.mod("item/" + block.getId().getPath());
            this.slabBlock((SlabBlock) actualBlock, blockLoc, blockLoc);
            ResourceLocation finalSlab = IdBuilder.mod("block/" + block.getId().getPath());
            this.itemModels().getBuilder(itemLoc.getPath())
                    .parent(new ModelFile.ExistingModelFile(finalSlab, this.itemModels().existingFileHelper));
        } else {
            this.simpleBlock(actualBlock);
            this.simpleBlockItem(actualBlock, this.cubeAll(actualBlock));
        }
    }

    private void pillarBlockAndItem(Block block) {
        // Models
        String nonNullPath = IdHelper.getNonNullPath(block);
        BlockModelBuilder base = this.models()
                .getBuilder("block/pillar/" + nonNullPath);

        ResourceLocation texture = IdBuilder.mod("block/" + IdHelper.getNonNullPath(block) + "_base");
        this.createCube(
                VectorHelper.getIntegerVector(1, 0, 1),
                VectorHelper.getIntegerVector(15, 16, 15), base)
                .texture("0", texture);


        ResourceLocation pillarUV = IdBuilder.mod("block/pillar/" + IdHelper.getNonNullPath(block) + "_cap");
        BlockModelBuilder topCap = this.models()
                .getBuilder("block/pillar/" + nonNullPath + "_top");
        this.createPillarTopCap(topCap)
                .texture("0", pillarUV);

        BlockModelBuilder bottomCap = this.models()
                .getBuilder("block/pillar/" + nonNullPath + "_bottom");
        this.createBottomCap(bottomCap)
                .texture("0", pillarUV);

        // Blockstates
        MultiPartBlockStateBuilder multipartBuilder = this.getMultipartBuilder(block);
        multipartBuilder.part().modelFile(base).addModel();
        multipartBuilder.part().modelFile(topCap).addModel()
                .condition(PillarBlock.HAS_TOP, false).end();
        multipartBuilder.part().modelFile(bottomCap).addModel()
                .condition(PillarBlock.HAS_BOTTOM, false).end();

        // Item
        ItemModelBuilder itemModel = this.itemModels().getBuilder("item/" + IdHelper.getNonNullPath(block));
        itemModel.parent(new ModelFile.ExistingModelFile(
                IdBuilder.mod("item/template/pillar_item_template"), this.itemModels().existingFileHelper))
                .texture("0", texture).texture("1", pillarUV);
    }

    private BlockModelBuilder createPillarTopCap(BlockModelBuilder topCap) {
        topCap.element()
                .from(0, 13, 0).to(16, 16, 16)
                .face(Direction.NORTH).texture("#0")
                .uvs(0f, 8f, 4f, 8.5f).end()
                .face(Direction.EAST).texture("#0")
                .uvs(8, 0, 12, 0.5f).end()
                .face(Direction.SOUTH).texture("#0")
                .uvs(8f, 0.5f, 12f, 1f).end()
                .face(Direction.WEST).texture("#0")
                .uvs(8f, 1f, 12f, 1.5f).end()
                .face(Direction.UP).texture("#0")
                .uvs(4f, 4f, 0f, 0f).end()
                .face(Direction.DOWN).texture("#0")
                .uvs(4f, 4f, 0f, 8f).end();
        return topCap;
    }

    private BlockModelBuilder createBottomCap(BlockModelBuilder topCap) {
        topCap.element()
                .from(0, 0, 0).to(16, 3, 16)
                .face(Direction.NORTH).texture("#0")
                .uvs(0f, 8f, 4f, 8.5f).end()
                .face(Direction.EAST).texture("#0")
                .uvs(8, 0, 12, 0.5f).end()
                .face(Direction.SOUTH).texture("#0")
                .uvs(8f, 0.5f, 12f, 1f).end()
                .face(Direction.WEST).texture("#0")
                .uvs(8f, 1f, 12f, 1.5f).end()
                .face(Direction.UP).texture("#0")
                .uvs(4f, 4f, 0f, 0f).end()
                .face(Direction.DOWN).texture("#0")
                .uvs(4f, 4f, 0f, 8f).end();
        return topCap;
    }

    private BlockModelBuilder createCube(Vector3i from, Vector3i to, BlockModelBuilder builder) {
        builder.element().from(from.getX(), from.getY(), from.getZ())
                .to(to.getX(), to.getY(), to.getZ())
                .face(Direction.NORTH).texture("#0")
                .uvs(0, 0, 14, 16).end()
                .face(Direction.WEST).texture("#0")
                .uvs(0, 0, 14, 16).end()
                .face(Direction.SOUTH).texture("#0")
                .uvs(0, 0, 14, 16).end()
                .face(Direction.EAST).texture("#0")
                .uvs(0, 0, 14, 16).end();
        return builder;
    }

}
