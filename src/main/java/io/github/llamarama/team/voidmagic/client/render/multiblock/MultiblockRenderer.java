package io.github.llamarama.team.voidmagic.client.render.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Optional;

public class MultiblockRenderer {

    private static final BlockPos RENDER_POS = new BlockPos(5, 5, 5);

    public static void addCustomRenders(final RenderWorldLastEvent event) {
        MatrixStack matrixStack = event.getMatrixStack();

        IRenderTypeBuffer.Impl buffer = VoidMagicClient.getGame().getRenderTypeBuffers().getBufferSource();

        render(matrixStack, buffer);

        buffer.finish(RenderType.getSolid());
    }

    public static void render(MatrixStack stack, IRenderTypeBuffer buffer) {
        BlockRendererDispatcher dispatcher = VoidMagicClient.getGame().getBlockRendererDispatcher();

        ClientWorld world =
                Optional.ofNullable(VoidMagicClient.getGame().world).orElseThrow(IllegalStateException::new);

        stack.push();
        stack.translate(RENDER_POS.getX(), RENDER_POS.getY(), RENDER_POS.getZ());
        dispatcher
                .renderBlock(ModBlocks.TOFAL.get().getDefaultState(), stack, buffer, 240, 200, EmptyModelData.INSTANCE);
        stack.pop();
    }

}
