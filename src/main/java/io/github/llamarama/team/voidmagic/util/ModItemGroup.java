package io.github.llamarama.team.voidmagic.util;

import io.github.llamarama.team.voidmagic.common.register.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModItemGroup extends ItemGroup {

    private static final ItemGroup GROUP = new ModItemGroup("voidmagic.group")
            .setBackgroundImage(IdBuilder.mod("textures/creative_tab.png"));

    private ModItemGroup(String label) {
        super(label);
    }

    public static ItemGroup get() {
        return GROUP;
    }

    @NotNull
    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.GUIDE_BOOK.get());
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

}
