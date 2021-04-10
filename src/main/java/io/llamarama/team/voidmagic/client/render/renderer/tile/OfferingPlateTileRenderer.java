package io.llamarama.team.voidmagic.client.render.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.llamarama.team.voidmagic.client.VoidMagicClient;
import io.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class OfferingPlateTileRenderer extends TileEntityRenderer<OfferingPlateTileEntity> {

    public OfferingPlateTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(OfferingPlateTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();

        matrixStackIn.translate(0.5f, 0.5f, 0.5f);
        AtomicReference<ItemStack> stack = new AtomicReference<>();
        tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
                (itemHandler) -> stack.set(itemHandler.getStackInSlot(0)));
        VoidMagicClient.getGame().getItemRenderer().renderItem(stack.get(),
                ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);

        matrixStackIn.pop();
    }

    @Override
    public boolean isGlobalRenderer(@NotNull OfferingPlateTileEntity te) {
        return super.isGlobalRenderer(te);
    }

}
