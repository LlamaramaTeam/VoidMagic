package io.github.llamarama.team.voidmagic.client.render.renderer.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import io.github.llamarama.team.voidmagic.common.util.config.ClientConfig;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

    @SuppressWarnings("deprecation")
    @Override
    public void render(OfferingPlateTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (ClientConfig.ENABLE_ITEM_RENDERING.get()) {
            matrixStackIn.push();

            final AtomicReference<ItemStack> stack = new AtomicReference<>();

            tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
                    (itemHandler) -> stack.set(itemHandler.getStackInSlot(0)));

            if (!stack.get().isEmpty()) {
                matrixStackIn.translate(0.5f, 0.5f, 0.5f);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(
                        tileEntityIn.rotationTick -= 360f / ClientConfig.TICKS_PER_ROTATION.get()));

                if (stack.get().getItem() == Items.DRAGON_EGG && ClientConfig.SPECIAL_BLOCK_RENDERING.get()) {
                    matrixStackIn.translate(-0.5f, 0, -0.5f);
                    VoidMagicClient.getGame().getBlockRendererDispatcher().renderBlock(
                            Blocks.DRAGON_EGG.getDefaultState(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
                } else {
                    matrixStackIn.scale(1.5f, 1.5f, 1.5f);
                    Item item = stack.get().getItem();
                    VoidMagicClient.getGame().getItemRenderer().renderItem(item.getDefaultInstance(),
                            ItemCameraTransforms.TransformType.GROUND,
                            combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                }

                if (tileEntityIn.rotationTick <= 0) {
                    tileEntityIn.rotationTick += 360;
                }
            }

            matrixStackIn.pop();
        }

    }

}
