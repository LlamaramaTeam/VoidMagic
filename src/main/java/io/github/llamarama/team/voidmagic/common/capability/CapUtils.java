package io.github.llamarama.team.voidmagic.common.capability;

import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.NonNullConsumer;

public class CapUtils {

    public static void executeForChaos(Chunk chunk, NonNullConsumer<IChaosHandler> consumer) {
        chunk.getCapability(VoidMagicCapabilities.CHAOS).ifPresent(consumer);
    }

}
