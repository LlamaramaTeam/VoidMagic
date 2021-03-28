package io.llamarama.team.voidmagic.common.register;

import io.llamarama.team.voidmagic.VoidMagic;
import io.llamarama.team.voidmagic.common.item.GuideBookItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public final class ModItems {

    public static final RegistryObject<Item> PUTILIAM = register("putiliam",
            () -> new Item(getDefaultProperties()));
    public static final RegistryObject<GuideBookItem> GUIDE_BOOK = register("guide_book",
            () -> new GuideBookItem(getDefaultProperties()));

    private static <I extends Item> RegistryObject<I> register(String id, Supplier<I> item) {
        return ModRegistries.ITEMS.register(id, item);
    }

    static void init(IEventBus bus) {
        ModRegistries.ITEMS.register(bus);
    }

    private static Item.Properties getDefaultProperties() {
        return new Item.Properties().tab(VoidMagic.GROUP);
    }

}
