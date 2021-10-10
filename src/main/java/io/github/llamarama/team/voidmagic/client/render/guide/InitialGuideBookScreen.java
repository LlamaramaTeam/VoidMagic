package io.github.llamarama.team.voidmagic.client.render.guide;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.network.packet.IncreaseChaosPacket;
import io.github.llamarama.team.voidmagic.common.network.packet.ReduceChaosPacket;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.List;

@Environment(EnvType.CLIENT)
public class InitialGuideBookScreen extends Screen {

    public static final String GUIDE_BOOK_SCREEN_KEY = "voidmagic.guide.screen";
    private static final List<Item> ENTRIES = ModItems.getModItems().values().asList();
    private final ItemStack bookStack;
    private ButtonWidget increaseButton;
    private ButtonWidget decreaseButton;


    public InitialGuideBookScreen(ItemStack bookStack) {
        super(new TranslatableText(GUIDE_BOOK_SCREEN_KEY));
        this.bookStack = bookStack;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        for (int i = 0; i < ENTRIES.size(); i++) {
            Item current = ENTRIES.get(i);
            VoidMagicClient.getGame().getItemRenderer().renderInGui(current.getDefaultStack(), 16 * i, 16);
        }
        int x = VoidMagicClient.getGame().getWindow().getWidth() / 2;
        int y = VoidMagicClient.getGame().getWindow().getHeight() / 2;

        VoidMagicClient.getGame().getItemRenderer().renderInGui(this.bookStack, x, y);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(32, 32, 0).color(0, 0, 0, 0).next();
        buffer.vertex(32, 64, 0).color(0, 0, 0, 0).next();
        buffer.vertex(64, 64, 0).color(0, 0, 0, 0).next();
        buffer.vertex(64, 32, 0).color(0, 0, 0, 0).next();
        buffer.end();

        BufferRenderer.draw(buffer);
    }

    @Override
    protected void init() {
        super.init();
        this.increaseButton = this.addDrawableChild(new ButtonWidget(width / 2, height / 2, 100, 20,
                new LiteralText("decrease"), (button) -> {

            VoidMagic.getLogger().info("Pogus Maximus");
            ClientPlayerEntity player = VoidMagicClient.getGame().player;

            if (player == null)
                return;

            ClientWorld world = player.networkHandler.getWorld();
            ModNetworking.get().sendToServer(
                    new ReduceChaosPacket(world.getChunk(player.getBlockPos()).getPos(), 10)
            );
        }));

        this.decreaseButton = this.addDrawableChild(new ButtonWidget(width / 2 - width / 5, height / 2 - height / 5, 100, 20,
                new LiteralText("increase"), (button) -> {
            ClientPlayerEntity player = VoidMagicClient.getGame().player;

            if (player == null)
                return;

            ClientWorld world = player.networkHandler.getWorld();
            ModNetworking.get().sendToServer(
                    new IncreaseChaosPacket(world.getChunk(player.getBlockPos()).getPos(), 10));
        }));
    }

}
