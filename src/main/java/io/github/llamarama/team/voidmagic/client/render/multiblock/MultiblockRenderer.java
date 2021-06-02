package io.github.llamarama.team.voidmagic.client.render.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.multiblock.ModMultiblocks;
import io.github.llamarama.team.voidmagic.common.multiblock.predicates.BlockStatePredicate;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;

public class MultiblockRenderer {

    public static void render(MatrixStack stack, IRenderTypeBuffer buffer, BlockPos center, int overlay) {
        stack.push();

        ModMultiblocks.FANCY.getKeys().forEach((pos, predicate) -> {
            if (predicate instanceof BlockStatePredicate) {
                BlockState state = ((BlockStatePredicate) predicate).getState();
                renderBlock(state, center.add(pos), stack, buffer, overlay);
            }
        });

        stack.pop();
    }

    private static void renderBlock(BlockState state, BlockPos pos, MatrixStack stack, IRenderTypeBuffer builder, int overlay) {
        ClientWorld world = VoidMagicClient.getGame().world;
        VoidMagicClient.getGame().getBlockRendererDispatcher()
                .renderBlock(state, stack, builder, world.getLightValue(pos), overlay, EmptyModelData.INSTANCE);
    }

}
