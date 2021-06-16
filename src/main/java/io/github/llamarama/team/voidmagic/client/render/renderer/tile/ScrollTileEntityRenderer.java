package io.github.llamarama.team.voidmagic.client.render.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.lib.spellbinding.CircleRegistry;
import io.github.llamarama.team.voidmagic.common.tile.ScrollTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ScrollTileEntityRenderer extends TileEntityRenderer<ScrollTileEntity> {

    public static final Map<ResourceLocation, ResourceLocation> TEXTURES =
            new HashMap<>(CircleRegistry.getRegistry().size());

    public ScrollTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        CircleRegistry.getRegistry().values().forEach(location -> {
            String path = "circles/" + location.getPath();
            String namespace = location.getNamespace();

            TEXTURES.put(location, new ResourceLocation(namespace + ":" + path));
        });
    }

    @Override
    public void render(ScrollTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft minecraft = VoidMagicClient.getGame();
        ISpellbindingCircle currentCircle = tileEntityIn.getCircle();

        if (currentCircle != null) {
            ResourceLocation currentCircleLoc = CircleRegistry.getRegistry().get(currentCircle);

            ResourceLocation textureLoc = TEXTURES.get(currentCircleLoc);
        }
    }

}
