package io.github.llamarama.team.voidmagic.common.capability.handler;

import net.minecraft.util.math.MathHelper;

public interface IChaosHandler {

    int getChaos();

    void setChaos(int newVal);

    default void consume(int amount) {
        int newVal = this.getChaos() - amount;

        if (newVal <= 0) {
            return;
        }

        this.setChaos(newVal);
    }

    default void increase(int amount) {
        float newVal = amount + getChaos();

        this.setChaos(MathHelper.floor(newVal));
    }


}
