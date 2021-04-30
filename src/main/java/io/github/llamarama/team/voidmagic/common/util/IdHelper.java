package io.github.llamarama.team.voidmagic.common.util;

import io.github.llamarama.team.voidmagic.common.util.constants.ModConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unused")
public final class IdHelper {

    private IdHelper() {
    }

    public static String getNonNullPath(Block block) {
        ResourceLocation loc = block.getRegistryName();
        if (loc != null) {
            return loc.getPath();
        }

        return ModConstants.EMPTY;
    }

    public static String getNonNullPath(Item item) {
        ResourceLocation loc = item.getRegistryName();
        if (loc != null) {
            return loc.getPath();
        }

        return ModConstants.EMPTY;
    }

    public static String getIdString(Item item) {
        ResourceLocation registryName = item.getRegistryName();

        if (registryName == null)
            return ModConstants.EMPTY;

        return registryName.toString();
    }

    public static String getIdString(Block block) {
        ResourceLocation registryName = block.getRegistryName();

        if (registryName == null)
            return ModConstants.EMPTY;

        return registryName.toString();
    }

    public static Optional<Block> getBlockFromID(String id) {
        ResourceLocation location = new ResourceLocation(id);
        Block out = ForgeRegistries.BLOCKS.getValue(location);

        return Optional.ofNullable(out);
    }

    public static Optional<Item> getItemFromID(String id) {
        ResourceLocation location = new ResourceLocation(id);
        Item out = ForgeRegistries.ITEMS.getValue(location);

        return Optional.ofNullable(out);
    }

    public static String getFullIdString(Block block) {
        Optional<ResourceLocation> id = Optional.ofNullable(block.getRegistryName());
        final AtomicReference<String> out = new AtomicReference<>(ModConstants.EMPTY);
        id.ifPresent((location) -> out.set(location.toString()));

        return out.get();
    }

}
