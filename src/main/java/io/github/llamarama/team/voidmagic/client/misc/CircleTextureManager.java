package io.github.llamarama.team.voidmagic.client.misc;

import com.google.common.collect.ImmutableMap;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import io.github.llamarama.team.voidmagic.common.lib.spellbinding.CircleRegistry;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CircleTextureManager {

    public static final CircleTextureManager INSTANCE = new CircleTextureManager();
    private final Map<ResourceLocation, ResourceLocation> circlePatterns;
    private final Map<ResourceLocation, ResourceLocation> scrollTopTextures;

    private CircleTextureManager() {
        ImmutableMap<ISpellbindingCircle, ResourceLocation> registry = CircleRegistry.getRegistry();

        this.circlePatterns = registry.values().stream()
                .map((circleId) -> {
                    String namespace = circleId.getNamespace();
                    String texturePath = "textures/circles/" + circleId.getPath();

                    return Pair.of(circleId, new ResourceLocation(namespace, texturePath));
                }).collect(HashMap::new, (map, pair) -> map.putIfAbsent(pair.getKey(), pair.getValue()), Map::putAll);

        this.scrollTopTextures = registry.values().stream()
                .map((circleId) -> {
                    String namespace = circleId.getNamespace();
                    String texturePath = "textures/scrolls/" + circleId.getPath();

                    return Pair.of(circleId, new ResourceLocation(namespace, texturePath));
                }).collect(HashMap::new, (map, pair) -> map.putIfAbsent(pair.getKey(), pair.getValue()), Map::putAll);
    }

    public static void init() {
    }

    public ResourceLocation getCircleTexture(@NotNull ResourceLocation location) {
        return this.circlePatterns.get(location);
    }

    public Optional<ResourceLocation> getCircleTexture(@Nullable ISpellbindingCircle circle) {
        if (circle == null)
            return Optional.empty();

        return Optional.ofNullable(this.getCircleTexture(CircleRegistry.getRegistry().get(circle)));
    }

    @Nullable
    public ResourceLocation getScrollPattern(@NotNull ResourceLocation location) {
        return this.scrollTopTextures.get(location);
    }

    @Nullable
    public Optional<ResourceLocation> getScrollPattern(ISpellbindingCircle circle) {
        if (circle == null)
            return Optional.empty();

        return Optional.ofNullable(this.getCircleTexture(CircleRegistry.getRegistry().get(circle)));
    }

}
