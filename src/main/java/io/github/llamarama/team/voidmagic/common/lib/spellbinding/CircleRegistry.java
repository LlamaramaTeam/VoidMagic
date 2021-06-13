package io.github.llamarama.team.voidmagic.common.lib.spellbinding;

import com.google.common.collect.ImmutableMap;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author 0xJoeMama
 * @since 2021
 */
public final class CircleRegistry {

    private static final Map<ISpellbindingCircle, ResourceLocation> INTERNAL_REGISTRY = new ConcurrentHashMap<>();
    public static final Map<ISpellbindingCircle, ResourceLocation> REGISTRY = ImmutableMap.copyOf(INTERNAL_REGISTRY);

    public static ISpellbindingCircle register(Supplier<ISpellbindingCircle> circle, ResourceLocation id) {
        INTERNAL_REGISTRY.put(circle.get(), id);

        return circle.get();
    }

}
