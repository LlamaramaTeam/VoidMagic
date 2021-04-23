package io.github.llamarama.team.voidmagic.client.render.guide;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public abstract class Page extends Screen {

    public Page(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

}
