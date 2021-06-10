package io.github.llamarama.team.voidmagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class CustomRenderType extends RenderType {

    public static final CustomRenderType GHOST_RENDER = new CustomRenderType(
            "ghost",
            DefaultVertexFormats.BLOCK,
            GL_QUADS, 256, false, false,
            () -> {
                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(
                        GlStateManager.SourceFactor.CONSTANT_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA
                );
                RenderSystem.blendColor(1, 1, 1, 0.4f);
            },
            () -> {
                RenderSystem.blendColor(1, 1, 1, 1);
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
            });


    public CustomRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

}
