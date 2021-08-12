package io.github.llamarama.team.voidmagic.mixin.client;

import io.github.llamarama.team.voidmagic.common.lib.chaos.ChaosProvider;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At("TAIL"))
    private void renderCustom(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ClientPlayerEntity player = this.client.player;
        if (player != null) {
            ItemStack stackInHand = player.getStackInHand(Hand.MAIN_HAND);
            ClientWorld world = this.client.world;

            if (stackInHand.getItem() == ModItems.GUIDE_BOOK && world != null) {
                Chunk chunk = world.getChunk(player.getBlockPos());
                LiteralText message = new LiteralText(Integer.toString(((ChaosProvider) chunk).getChaosValue()));
                player.sendMessage(message, true);
            }
        }
    }

}
