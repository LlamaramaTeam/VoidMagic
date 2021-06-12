package io.github.llamarama.team.voidmagic.api.client.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockProvider;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.client.render.CustomRenderType;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.predicates.BlockStatePredicate;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;

public interface IMultiblockRenderer<T extends IMultiblockProvider> {

    default void renderStructure(IMultiblockType type, MatrixStack stack, IRenderTypeBuffer buffer, BlockPos center,
                                 int overlay) {
        IVertexBuilder vertexBuilder = buffer.getBuffer(CustomRenderType.GHOST_RENDER);
        type.getDefaultKeys().forEach((pos, predicate) -> {
            if (predicate instanceof BlockStatePredicate) {
                BlockState state = ((BlockStatePredicate) predicate).getState();
                renderBlock(state, pos.add(center), stack, vertexBuilder);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    default void renderBlock(BlockState state, BlockPos pos, MatrixStack stack, IVertexBuilder builder) {
        stack.push();
        stack.translate(pos.getX(), pos.getY(), pos.getZ());
        ClientWorld world = VoidMagicClient.getGame().world;
        VoidMagicClient.getGame().getBlockRendererDispatcher()
                .renderModel(state, pos, world, stack, builder, true, world.rand, EmptyModelData.INSTANCE);

        stack.pop();
    }

}
