package io.llamarama.team.voidmagic.common.register;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public final class ModItems {

    public static final RegistryObject<BlockItem> PUTILIAM = register("putiliam",
            () -> new BlockItem(ModBlocks.PUTILIAM.get(), getDefaultProperties()));

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
