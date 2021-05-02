package io.github.llamarama.team.voidmagic.client.render.guide;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.network.packet.IncreaseChaosPacket;
import io.github.llamarama.team.voidmagic.common.network.packet.ReduceChaosPacket;
import io.github.llamarama.team.voidmagic.common.register.ModRegistries;
import io.github.llamarama.team.voidmagic.common.util.constants.CustomTranslations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class InitialGuideBookScreen extends Screen {

    private final ItemStack itemInHand;
    private final List<Item> entries;

    public InitialGuideBookScreen(ItemStack itemInHand) {
        super(new TranslationTextComponent(CustomTranslations.GUIDE_BOOK_SCREEN.get()));
        this.itemInHand = itemInHand;
        this.entries =
                ModRegistries.ITEMS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.addButton(new Button(width / 2, height / 2, 100, 20,
                new StringTextComponent("decrease"), (button) -> {
            ClientPlayerEntity player = minecraft.player;

            if (player == null)
                return;

            ClientWorld world = player.connection.getWorld();
            ModNetworking.get().sendToServer(
                    new ReduceChaosPacket(10, world.getChunkAt(player.getPosition()))
            );
        }));

        this.addButton(new Button(width / 2 - width / 5, height / 2 - height / 5, 100, 20,
                new StringTextComponent("increase"), (button) -> {
            ClientPlayerEntity player = minecraft.player;

            if (player == null)
                return;

            ClientWorld world = player.connection.getWorld();
            ModNetworking.get().sendToServer(
                    new IncreaseChaosPacket(world.getChunkAt(player.getPosition()).getPos(), 10));
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
