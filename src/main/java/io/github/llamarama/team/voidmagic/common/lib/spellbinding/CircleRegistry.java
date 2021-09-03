package io.github.llamarama.team.voidmagic.common.lib.spellbinding;

import com.google.common.collect.ImmutableMap;
import io.github.llamarama.team.voidmagic.api.spellbinding.SpellbindingCircle;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class CircleRegistry {

    private static final Map<SpellbindingCircle, Identifier> INTERNAL_REGISTRY = new HashMap<>();

    public static SpellbindingCircle register(Supplier<SpellbindingCircle> circleSupplier, Identifier id) {
        INTERNAL_REGISTRY.putIfAbsent(circleSupplier.get(), id);

        return circleSupplier.get();
    }

    public static ImmutableMap<SpellbindingCircle, Identifier> getRegistry() {
        return ImmutableMap.copyOf(INTERNAL_REGISTRY);
    }

}
