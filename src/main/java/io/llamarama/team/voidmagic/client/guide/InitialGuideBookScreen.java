package io.llamarama.team.voidmagic.client.guide;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.llamarama.team.voidmagic.client.VoidMagicClient;
import io.llamarama.team.voidmagic.common.register.ModBlocks;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import io.llamarama.team.voidmagic.util.IdBuilder;
import io.llamarama.team.voidmagic.util.constants.CustomTranslations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class InitialGuideBookScreen extends Screen {

    private final ResourceLocation TEXTURE;
    private final ItemStack itemInHand;
    private final List<Item> entries;

    public InitialGuideBookScreen(ItemStack itemInHand) {
        super(new TranslationTextComponent(CustomTranslations.GUIDE_BOOK_SCREEN.get()));
        this.itemInHand = itemInHand;
        this.TEXTURE = IdBuilder.mc("textures/block/dirt.png");
        this.entries =
                ModRegistries.ITEMS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.addButton(new Button(0, 0, 20, 40,
                new TranslationTextComponent("button"), (button) -> {
            ClientPlayerEntity player = VoidMagicClient.getGame().player;

            if (player != null) {
                player.sendChatMessage("hallo");
            }

            VoidMagicClient.getGame().getBlockRendererDispatcher()
                    .renderBlock(ModBlocks.TOFAL.get().getDefaultState(), new MatrixStack(),
                            minecraft.getRenderTypeBuffers().getBufferSource(), 10, 1);
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        for (int i = 0; i < entries.size(); i++) {
            Item current = entries.get(i);
            VoidMagicClient.getGame().getItemRenderer().renderItemIntoGUI(current.getDefaultInstance(), 16 * i, 16);
        }
        int x = VoidMagicClient.getGame().getMainWindow().getWidth() / 2;
        int y = VoidMagicClient.getGame().getMainWindow().getHeight() / 2;

        VoidMagicClient.getGame().getItemRenderer().renderItemIntoGUI(this.itemInHand, x, y);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(32, 32, 0).color(0, 0, 0, 0).endVertex();
        buffer.pos(32, 64, 0).color(0, 0, 0, 0).endVertex();
        buffer.pos(64, 64, 0).color(0, 0, 0, 0).endVertex();
        buffer.pos(64, 32, 0).color(0, 0, 0, 0).endVertex();
        buffer.finishDrawing();
        WorldVertexBufferUploader.draw(buffer);
    }

}