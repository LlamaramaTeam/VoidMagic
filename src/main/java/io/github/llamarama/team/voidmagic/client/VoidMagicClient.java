package io.github.llamarama.team.voidmagic.client;

import io.github.llamarama.team.voidmagic.client.register.ModBlockEntityRenderers;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class VoidMagicClient implements ClientModInitializer {

    public static MinecraftClient getGame() {
        return MinecraftClient.getInstance();
    }

    @Override
    public void onInitializeClient() {
        this.setupRenderTypes();

        ModBlockEntityRenderers.associateRenderers();
    }

    private void setupRenderTypes() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHALK, RenderLayer.getCutoutMipped());
    }

}
