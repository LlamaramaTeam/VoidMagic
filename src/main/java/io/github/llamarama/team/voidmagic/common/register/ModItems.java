package io.github.llamarama.team.voidmagic.common.register;

import io.github.llamarama.team.voidmagic.common.item.GuideBookItem;
import io.github.llamarama.team.voidmagic.common.item.SpellbindingFabricItem;
import io.github.llamarama.team.voidmagic.util.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ModItems {

    // Used to get item properties instead of methods. It's just easier.
    public static final Supplier<Item.Properties> DEFAULT = () -> new Item.Properties().group(ModItemGroup.get());
    public static final Supplier<Item.Properties> UNSTACKABLE = () -> DEFAULT.get().maxStackSize(1);

    // Items
    public static final RegistryObject<Item> PUTILIAM = register("putiliam",
            () -> new Item(DEFAULT.get()));
    public static final RegistryObject<GuideBookItem> GUIDE_BOOK = register("guide_book",
            () -> new GuideBookItem(UNSTACKABLE.get()));
    public static final RegistryObject<SpellbindingFabricItem> SPELLBINDING_FABRIC = register("spellbinding_fabric",
            () -> new SpellbindingFabricItem(DEFAULT.get()));

    private static <I extends Item> RegistryObject<I> register(String id, Supplier<I> item) {
        return ModRegistries.ITEMS.register(id, item);
    }

    static void init(IEventBus bus) {
        ModRegistries.ITEMS.register(bus);
    }

}
