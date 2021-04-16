package io.github.llamarama.team.voidmagic.client.guide;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public abstract class Chapter implements List<Page> {

    private final List<Screen> pages;

    public Chapter() {
        this.pages = Lists.newArrayList();
    }

    protected <PG extends Screen> Chapter addPage(PG page) {
        this.pages.add(page);
        return this;
    }

    protected abstract void fillPages();

}
