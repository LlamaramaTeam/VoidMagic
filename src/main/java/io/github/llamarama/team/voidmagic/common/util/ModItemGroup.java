package io.github.llamarama.team.voidmagic.common.util;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

public final class ModItemGroup {

    private static final ItemGroup MOD_GROUP = FabricItemGroupBuilder
            .build(IdBuilder.of("group"), () -> new ItemStack(Items.WHEAT));

    @NotNull
    public static ItemGroup get() {
        return MOD_GROUP;
    }

}
