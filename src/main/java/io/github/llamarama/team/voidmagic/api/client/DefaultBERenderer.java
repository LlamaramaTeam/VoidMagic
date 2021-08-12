package io.github.llamarama.team.voidmagic.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

@Environment(EnvType.CLIENT)
public abstract class DefaultBERenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {

    public DefaultBERenderer(BlockEntityRendererFactory.Context ctx) {

    }

}
