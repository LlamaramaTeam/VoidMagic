package io.github.llamarama.team.voidmagic.client.render.multiblock;

import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockRotation;
import io.github.llamarama.team.voidmagic.api.multiblock.MultiblockType;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockStatePredicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class MultiblockRenderer {

    public static void render(MultiblockType type, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertices) {
        type.getKeysFor(MultiblockRotation.ZERO).forEach((blockPos, predicate) -> {
            BlockPos modifiedPos = blockPos.add(pos);

            if (predicate instanceof BlockStatePredicate blockStatePredicate) {
                BlockState state = blockStatePredicate.state();

                MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(
                        MinecraftClient.getInstance().world,
                        MinecraftClient.getInstance().getBlockRenderManager().getModel(state),
                        state,
                        modifiedPos,
                        matrices,
                        vertices.getBuffer(RenderLayer.getSolid()),
                        true,
                        MinecraftClient.getInstance().world.random,
                        0x2323332,
                        OverlayTexture.field_32953);
            }
        });
    }

}
