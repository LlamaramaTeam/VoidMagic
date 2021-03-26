package io.llamarama.team.voidmagic.common.register;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModRegistries {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VoidMagic.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VoidMagic.MODID);


    private ModRegistries() {
    }

    public static void initRegistries(IEventBus bus) {
        ModBlocks.init(bus);
        ModItems.init(bus);
    }

}
