package io.github.llamarama.team.voidmagic.common.capability.handler;

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

    int getMaxChaos();

    void setMaxChaos(int max);

}
