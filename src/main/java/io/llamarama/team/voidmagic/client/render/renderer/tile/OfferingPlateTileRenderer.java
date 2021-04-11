package io.llamarama.team.voidmagic.client.render.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.llamarama.team.voidmagic.client.VoidMagicClient;
import io.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.concurrent.atomic.AtomicReference;

@OnlyIn(Dist.CLIENT)
public class OfferingPlateTileRenderer extends TileEntityRenderer<OfferingPlateTileEntity> {

    public OfferingPlateTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(OfferingPlateTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();

        matrixStackIn.translate(0.5f, 0.5f, 0.5f);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.rotationTick--));

        AtomicReference<ItemStack> stack = new AtomicReference<>();
        tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
                (itemHandler) -> stack.set(itemHandler.getStackInSlot(0)));
        Item item = stack.get().getItem();
        VoidMagicClient.getGame().getItemRenderer().renderItem(item.getDefaultInstance(),
                ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);

        matrixStackIn.pop();

        if (tileEntityIn.rotationTick <= 0) {
            tileEntityIn.rotationTick += 360;
        }
    }

}
