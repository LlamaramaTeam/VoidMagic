package com.github.llamarama.team.common.register;

import com.github.llamarama.team.common.util.IdBuilder;
import com.github.llamarama.team.common.util.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class ModItems {

    public static final Supplier<Item.Settings> DEFAULT = () -> new Item.Settings().group(ModItemGroup.get());
    public static final Supplier<Item.Settings> UNSTACKABLE = () -> DEFAULT.get().maxCount(1);
    public static final Supplier<Item.Settings> QUARTER_STACKABLE = () -> DEFAULT.get().maxCount(16);

    private static final Map<String, Item> REGISTRY = new ConcurrentHashMap<>();

    private ModItems() {
    }

    @NotNull
    private static Item register(String id, Item item) {
        REGISTRY.putIfAbsent(id, item);
        return item;
    }

    static void init() {
        REGISTRY.forEach((id, item) -> Registry.register(Registry.ITEM, IdBuilder.of(id), item));
    }

}
