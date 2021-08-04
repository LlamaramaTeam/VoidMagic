package com.github.llamarama.team.client;

import com.github.llamarama.team.common.register.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class VoidMagicClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        this.setupRenderTypes();
    }

    private void setupRenderTypes() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHALK, RenderLayer.getCutoutMipped());
    }

}
