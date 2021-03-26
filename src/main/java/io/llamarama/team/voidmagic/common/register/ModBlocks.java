package io.llamarama.team.voidmagic.common.register;

import io.llamarama.team.voidmagic.VoidMagic;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public final class ModBlocks {

    public static final RegistryObject<RedstoneWireBlock> PUTILIAM = registerNoItem("putiliam_wire",
            () -> new RedstoneWireBlock(AbstractBlock.Properties.copy(Blocks.REDSTONE_WIRE)));

    private ModBlocks() {
    }

    private static <B extends Block> RegistryObject<B> register(String id, Supplier<B> block) {
        RegistryObject<B> out = registerNoItem(id, block);
        ModRegistries.ITEMS.register(out.getId().getPath(), () -> new BlockItem(out.get(), new Item.Properties().tab(ItemGroup.TAB_MISC).tab(VoidMagic.GROUP)));
        return out;
    }

    private static <B extends Block> RegistryObject<B> registerNoItem(String id, Supplier<B> block) {
        return ModRegistries.BLOCKS.register(id, block);
    }

    public static void init(IEventBus bus) {
        ModRegistries.BLOCKS.register(bus);
    }

}
