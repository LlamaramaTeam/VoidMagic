package io.github.llamarama.team.voidmagic.client.render.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.client.misc.CircleTextureManager;
import io.github.llamarama.team.voidmagic.common.tile.ScrollTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public class ScrollTileEntityRenderer extends TileEntityRenderer<ScrollTileEntity> {

    public ScrollTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ScrollTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft minecraft = VoidMagicClient.getGame();
        ISpellbindingCircle currentCircle = tileEntityIn.getCircle();

        Optional<ResourceLocation> sprite = CircleTextureManager.INSTANCE.getCircleTexture(currentCircle);
    }

}
