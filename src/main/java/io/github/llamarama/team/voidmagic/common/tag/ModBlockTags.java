package io.github.llamarama.team.voidmagic.common.tag;

import io.github.llamarama.team.voidmagic.common.util.IdBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public final class ModBlockTags {


    private ModBlockTags() {

    }

    public static Tag<Block> createTag(String id) {
        return TagRegistry.block(IdBuilder.of(id));
    }

}
