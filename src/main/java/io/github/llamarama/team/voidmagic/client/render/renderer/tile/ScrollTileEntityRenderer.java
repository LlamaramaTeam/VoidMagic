package io.github.llamarama.team.voidmagic.client.render.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.tile.ScrollTileEntity;
import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ScrollTileEntityRenderer extends TileEntityRenderer<ScrollTileEntity> {

    public static final ResourceLocation SCROLL_PAGE = IdBuilder.mod("block/scroll/scroll_page");
    private static final float NINETY_DEG = (float) (Math.PI / 2);

    public ScrollTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ScrollTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.getBlockState().get(ModBlockProperties.OPEN)) {
            Direction direction = tileEntityIn.getBlockState().get(HorizontalBlock.HORIZONTAL_FACING);
            this.renderScrollOpen(matrixStackIn, bufferIn, direction);
        }

        if (tileEntityIn.isCrafting())
            this.renderCraftingProcess(tileEntityIn, matrixStackIn, bufferIn);
    }

    private void renderCraftingProcess(ScrollTileEntity tileEntity, MatrixStack matrices, IRenderTypeBuffer buffer) {

    }

    @SuppressWarnings({"deprecation"})
    protected void renderScrollOpen(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, Direction direction) {
        IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderType.getCutout());

        TextureAtlasSprite sprite =
                VoidMagicClient.getGame().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
                        .apply(SCROLL_PAGE);

        // add(POSITION_3F).add(COLOR_4UB).add(TEX_2F).add(TEX_2SB).add(NORMAL_3B)
        matrixStackIn.push();
        matrixStackIn.translate(0.5d, 0, 0.5d);
        if (direction.getAxis() == Direction.Axis.X) {
            matrixStackIn.rotate(Vector3f.YP.rotation(NINETY_DEG));
        }

        // UP
        this.addPCTLNVertex(matrixStackIn, 0.5f, 0.001f, -1.5f, sprite.getMinU(), sprite.getMaxV(), vertexBuilder);
        this.addPCTLNVertex(matrixStackIn, 0.5f, 0.001f, 1.5f, sprite.getMaxU(), sprite.getMaxV(), vertexBuilder);
        this.addPCTLNVertex(matrixStackIn, -0.5f, 0.001f, 1.5f, sprite.getMaxU(), sprite.getMinV(), vertexBuilder);
        this.addPCTLNVertex(matrixStackIn, -0.5f, 0.001f, -1.5f, sprite.getMinU(), sprite.getMinV(), vertexBuilder);

        // DOWN
        this.addPCTLNVertex(matrixStackIn, -0.5f, 0.001f, -1.5f, sprite.getMinU(), sprite.getMinV(), vertexBuilder);
        this.addPCTLNVertex(matrixStackIn, -0.5f, 0.001f, 1.5f, sprite.getMaxU(), sprite.getMinV(), vertexBuilder);
        this.addPCTLNVertex(matrixStackIn, 0.5f, 0.001f, 1.5f, sprite.getMaxU(), sprite.getMaxV(), vertexBuilder);
        this.addPCTLNVertex(matrixStackIn, 0.5f, 0.001f, -1.5f, sprite.getMinU(), sprite.getMaxV(), vertexBuilder);

        matrixStackIn.pop();
    }

    @SuppressWarnings("SameParameterValue")
    protected void addPCTLNVertex(MatrixStack matrices, float x, float y, float z, float u, float v,
                                  IVertexBuilder builder) {
        // add(POSITION_3F).add(COLOR_4UB).add(TEX_2F).add(TEX_2SB).add(NORMAL_3B)
        builder.pos(matrices.getLast().getMatrix(), x, y, z)
                .color(255, 255, 255, 255)
                .tex(u, v)
                .lightmap(240)
                .normal(1, 0, 0)
                .endVertex();
    }

}
