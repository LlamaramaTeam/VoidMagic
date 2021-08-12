package io.github.llamarama.team.voidmagic.common.tag;

import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public final class ModItemTags {


    private ModItemTags() {

    }

    public static Tag<Item> createTag(String id) {
        return TagRegistry.item(IdBuilder.of(id));
    }

}
