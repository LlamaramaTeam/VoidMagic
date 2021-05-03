package io.github.llamarama.team.voidmagic.datagen.provider.assets;

import com.google.common.collect.Sets;
import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.common.block.PillarBlock;
import io.github.llamarama.team.voidmagic.common.block.PlateBlock;
import io.github.llamarama.team.voidmagic.common.block.ScrollBlock;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.common.register.ModRegistries;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import io.github.llamarama.team.voidmagic.common.util.IdHelper;
import io.github.llamarama.team.voidmagic.common.util.VectorHelper;
import net.minecraft.block.Block;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Set;
import java.util.stream.Stream;

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
        this.blacklist.add(ModBlocks.CHALK.get());
        this.blacklist.add(ModBlocks.DECORATIVE_PACKED_BLOCK.get());
        this.blacklist.add(ModBlocks.SCROLL.get());

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

        this.plateBlockModel(ModBlocks.OFFERING_PLATE.get(), IdBuilder.mod("block/withered_stone"));
        this.createSimpleBlockAndItem(ModBlocks.DECORATIVE_PACKED_BLOCK.get(), IdBuilder.mod("block/packed_block"));
        this.scrollBlock(ModBlocks.SCROLL.get());

        VoidMagic.getLogger().info("Added all default block models.");
    }

    private void scrollBlock(ScrollBlock scrollBlock) {
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(scrollBlock);

        // Getting the existing models.
        ModelFile.ExistingModelFile closedScroll =
                new ModelFile.ExistingModelFile(IdBuilder.mod("block/scroll/closed_scroll"),
                        this.models().existingFileHelper);
        ModelFile.ExistingModelFile openScroll =
                new ModelFile.ExistingModelFile(IdBuilder.mod("block/scroll/open_scroll"),
                        this.models().existingFileHelper);

        // Closed scroll stuff.
        builder.part().modelFile(closedScroll).rotationY(90).addModel()
                .condition(ModBlockProperties.OPEN, false)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH)
                .end();
        builder.part().modelFile(closedScroll).rotationY(180).addModel()
                .condition(ModBlockProperties.OPEN, false)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.EAST)
                .end();
        builder.part().modelFile(closedScroll).rotationY(270).addModel()
                .condition(ModBlockProperties.OPEN, false)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH)
                .end();
        builder.part().modelFile(closedScroll).addModel()
                .condition(ModBlockProperties.OPEN, false)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.WEST)
                .end();

        // Open scroll stuff
        builder.part().modelFile(openScroll).rotationY(90).addModel()
                .condition(ModBlockProperties.OPEN, true)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH)
                .end();
        builder.part().modelFile(openScroll).rotationY(180).addModel()
                .condition(ModBlockProperties.OPEN, true)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.EAST)
                .end();
        builder.part().modelFile(openScroll).rotationY(270).addModel()
                .condition(ModBlockProperties.OPEN, true)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH)
                .end();
        builder.part().modelFile(openScroll).addModel()
                .condition(ModBlockProperties.OPEN, true)
                .condition(HorizontalBlock.HORIZONTAL_FACING, Direction.WEST)
                .end();
    }

    private void plateBlockModel(Block block) {
        String path = IdHelper.getNonNullPath(((PlateBlock) block).getTarget());
        this.plateBlockModel(block, IdBuilder.mod("block/" + path));
    }

    private void plateBlockModel(Block block, ResourceLocation texture) {
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

        Stream.of(ModelBuilder.Perspective.values()).forEach((perspective) -> {
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

    private <B extends Block> void createSimpleBlockAndItem(B block, ResourceLocation texture) {
        BlockModelBuilder builder = this.models().getBuilder(IdHelper.getNonNullPath(block));

        builder.parent(new ModelFile.ExistingModelFile(IdBuilder.mc("block/cube_all"),
                this.models().existingFileHelper)).texture("all", texture).texture("particle", texture);

        this.getVariantBuilder(block).forAllStates((state) -> ConfiguredModel.builder().modelFile(builder).build());

        this.simpleBlockItem(block, builder);
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
