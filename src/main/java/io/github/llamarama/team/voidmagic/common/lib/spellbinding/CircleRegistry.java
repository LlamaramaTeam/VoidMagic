package io.github.llamarama.team.voidmagic.common.lib.spellbinding;

import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 0xJoeMama
 * @since 2021
 */
public final class CircleRegistry {

    public static final Map<? super ISpellbindingCircle, ResourceLocation> REGISTRY = new ConcurrentHashMap<>();

    public static void register(ISpellbindingCircle circle, ResourceLocation id) {
        REGISTRY.put(circle, id);
    }

}
