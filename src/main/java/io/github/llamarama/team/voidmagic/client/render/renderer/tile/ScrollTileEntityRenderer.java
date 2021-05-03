package io.github.llamarama.team.voidmagic.client.render.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.llamarama.team.voidmagic.common.tile.ScrollTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class ScrollTileEntityRenderer extends TileEntityRenderer<ScrollTileEntity> {

    public ScrollTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ScrollTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }

}
