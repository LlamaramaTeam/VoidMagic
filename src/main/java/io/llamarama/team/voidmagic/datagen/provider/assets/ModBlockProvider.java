package io.llamarama.team.voidmagic.datagen.provider.assets;

import com.google.common.collect.Sets;
import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.block.PillarBlock;
import io.llamarama.team.voidmagic.common.block.PlateBlock;
import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import io.llamarama.team.voidmagic.util.IdBuilder;
import io.llamarama.team.voidmagic.util.IdHelper;
import io.llamarama.team.voidmagic.util.VectorHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.Set;

public class ModBlockProvider extends BlockStateProvider {

    private final Set<Block> blacklist;

    public ModBlockProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, VoidMagic.MOD_ID, exFileHelper);
        this.blacklist = Sets.newHashSet();
    }

    @Override
    protected void registerStatesAndModels() {
        this.blacklist.add(ModBlocks.WITHERED_STONE_PILLAR.get());
        this.blacklist.add(ModBlocks.WITHERED_STONE_PLATE.get());
        this.blacklist.add(ModBlocks.TOFAL_PLATE.get());
        this.blacklist.add(ModBlocks.OFFERING_PLATE.get());

        ModRegistries.BLOCKS.getEntries().stream()
                .filter((block) -> !this.blacklist.contains(block.get()))
                .forEach(this::simpleBlockAndItem);

        this.blacklist.forEach((block) -> {
            if (block instanceof PillarBlock) {
                this.pillarBlockAndItem(block);
            } else if (block instanceof PlateBlock && block != ModBlocks.OFFERING_PLATE.get()) {
                this.plateBlockModel(block);
            }
        });

        this.plateBlockModel(ModBlocks.OFFERING_PLATE.get());

        VoidMagic.getLogger().info("Added all default block models.");
    }

    private void plateBlockModel(Block block) {
        Block target = ((PlateBlock) block).getTarget();
        ResourceLocation texture = IdBuilder.mod("block/" + IdHelper.getNonNullPath(target));
        BlockModelBuilder builder =
                this.models().getBuilder("block/" + IdHelper.getNonNullPath(block))
                        .element().from(0, 0, 0).to(16, 4, 16)
                        .face(Direction.UP)
                        .texture("#0").uvs(0, 0, 16, 16).end()
                        .face(Direction.DOWN)
                        .texture("#0").uvs(0, 0, 16, 16).end()
                        .face(Direction.EAST)
                        .uvs(0, 7, 16, 11).texture("#0").end()
                        .face(Direction.WEST)
                        .uvs(0, 7, 16, 11).texture("#0").end()
                        .face(Direction.NORTH)
                        .uvs(0, 7, 16, 11).texture("#0").end()
                        .face(Direction.SOUTH)
                        .uvs(0, 7, 16, 11).texture("#0").end()
                        .end()
                        .texture("0", texture)
                        .texture("particle", texture);

        this.getVariantBuilder(block).partialState().addModels(new ConfiguredModel(builder));

        ModelBuilder<ItemModelBuilder>.TransformsBuilder itemBuilder =
                this.itemModels().getBuilder("item/" + IdHelper.getNonNullPath(block))
                        .parent(builder).transforms();

        Arrays.stream(ModelBuilder.Perspective.values()).forEach((perspective) -> {
            if (perspective == ModelBuilder.Perspective.FIRSTPERSON_LEFT || perspective == ModelBuilder.Perspective.FIRSTPERSON_RIGHT) {
                itemBuilder.transform(perspective)
                        .rotation(0, 45, 0)
                        .scale(0.4f, 0.4f, 0.4f)
                        .end();
            } else if (perspective == ModelBuilder.Perspective.THIRDPERSON_LEFT || perspective == ModelBuilder.Perspective.THIRDPERSON_RIGHT) {
                itemBuilder.transform(perspective)
                        .rotation(75f, 45f, 0f)
                        .translation(0, 2.5f, 1.25f)
                        .scale(0.375f, 0.375f, 0.375f)
                        .end();
            } else if (perspective == ModelBuilder.Perspective.GROUND) {
                itemBuilder.transform(perspective)
                        .translation(0f, 3f, 0f)
                        .scale(0.25f, 0.25f, 0.25f)
                        .end();
            } else if (perspective == ModelBuilder.Perspective.GUI) {
                itemBuilder.transform(perspective)
                        .rotation(30f, 225f, 0f)
                        .scale(0.625f, 0.625f, 0.625f)
                        .end();
            } else {
                itemBuilder.transform(ModelBuilder.Perspective.FIXED)
                        .scale(0.5f, 0.5f, 0.5f);
            }
        });
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
        } else if (actualBlock instanceof StairsBlock) {
            String nonNullPath = IdHelper.getNonNullPath(actualBlock);
            this.stairsBlock((StairsBlock) actualBlock,
                    IdBuilder.mod("block/" +
                            nonNullPath.replaceAll("_stairs", "s")));
            String itemPath = "item/" + nonNullPath;
            this.itemModels().getBuilder(itemPath)
                    .parent(new ModelFile.ExistingModelFile(
                            IdBuilder.mod("block/" + nonNullPath),
                            this.itemModels().existingFileHelper));
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
                .texture("0", texture)
                .texture("particle", texture);


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
