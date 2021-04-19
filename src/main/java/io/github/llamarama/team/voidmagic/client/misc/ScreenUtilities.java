package io.github.llamarama.team.voidmagic.client.misc;

import io.github.llamarama.team.voidmagic.client.VoidMagicClient;
import io.github.llamarama.team.voidmagic.client.render.guide.InitialGuideBookScreen;
import net.minecraft.item.ItemStack;

public class ScreenUtilities {

    public static void openInitialBookScreen(ItemStack stack) {
        VoidMagicClient.getGame().displayGuiScreen(new InitialGuideBookScreen(stack));
    }

}
