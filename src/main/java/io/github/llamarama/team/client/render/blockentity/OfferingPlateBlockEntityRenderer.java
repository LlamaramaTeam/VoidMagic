package io.github.llamarama.team.client.render.blockentity;

import io.github.llamarama.team.api.client.DefaultBERenderer;
import io.github.llamarama.team.client.VoidMagicClient;
import io.github.llamarama.team.common.tile.OfferingPlateBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class OfferingPlateBlockEntityRenderer<T extends OfferingPlateBlockEntity> extends DefaultBERenderer<T> {

    public OfferingPlateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
//        if (ClientConfig.ENABLE_ITEM_RENDERING.get()) {
        // TODO Fix this once the config thing is figured out.
        matrices.push();

        ItemStack stack = entity.getStack(0);

        if (!stack.isEmpty()) {
            matrices.translate(0.5f, 0.5f, 0.5f);
            //noinspection PointlessArithmeticExpression
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(
                    entity.rotationTick -= 360f / 360f/*ClientConfig.TICKS_PER_ROTATION.get()*/));

            if (stack.getItem() == Items.DRAGON_EGG /*&& ClientConfig.SPECIAL_BLOCK_RENDERING.get()*/) {
                matrices.translate(-0.5f, 0, -0.5f);
                VoidMagicClient.getGame().getBlockRenderManager().renderBlockAsEntity(
                        Blocks.DRAGON_EGG.getDefaultState(), matrices, vertexConsumers, light, overlay);
            } else {
                matrices.scale(1.5f, 1.5f, 1.5f);
                Item item = stack.getItem();
                VoidMagicClient.getGame().getItemRenderer().renderItem(item.getDefaultStack(),
                        ModelTransformation.Mode.GROUND,
                        light, overlay, matrices, vertexConsumers, 0);
            }

            if (entity.rotationTick <= 0) {
                entity.rotationTick += 360;
            }
        }

        matrices.pop();
//        }
    }

}
