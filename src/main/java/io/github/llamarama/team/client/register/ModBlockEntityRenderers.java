package io.github.llamarama.team.client.register;

import io.github.llamarama.team.client.render.blockentity.OfferingPlateBlockEntityRenderer;
import io.github.llamarama.team.common.register.ModBlockEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class ModBlockEntityRenderers {

    private ModBlockEntityRenderers() {
    }

    public static void associateRenderers() {
        associate(ModBlockEntityTypes.OFFERING_PLATE, OfferingPlateBlockEntityRenderer::new);
    }

    private static <T extends BlockEntity> void associate(BlockEntityType<T> type,
                                                          Function<BlockEntityRendererFactory.Context,
                                                                  BlockEntityRenderer<T>> factory) {
        BlockEntityRendererRegistry.INSTANCE.register(type, factory::apply);
    }

}
