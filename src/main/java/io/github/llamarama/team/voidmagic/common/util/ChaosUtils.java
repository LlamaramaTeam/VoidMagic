package io.github.llamarama.team.voidmagic.common.util;

import io.github.llamarama.team.voidmagic.common.lib.chaos.ChaosProvider;
import net.minecraft.world.chunk.Chunk;

import java.util.function.Consumer;

public final class ChaosUtils {

    public static void executeForChaos(Chunk chunk, Consumer<ChaosProvider> consumer) {
        if (chunk instanceof ChaosProvider chaosProvider) {
            consumer.accept(chaosProvider);
        }
    }

}
