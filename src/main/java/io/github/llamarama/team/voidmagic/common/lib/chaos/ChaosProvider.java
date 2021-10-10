package io.github.llamarama.team.voidmagic.common.lib.chaos;

/**
 * Used to mark an NBT serializable as a provider of chaos.
 * TODO: Config Stuff
 *
 * @author 0xJoeMama
 */
public interface ChaosProvider {

    int getChaosValue();

    void setChaosValue(int newVal);

    default void consume(int amount) {
        int newVal = this.getChaosValue() - amount;

        if (newVal < 100 /*ServerConfig.MIN_CHAOS.get()*/)
            return;

        this.setChaosValue(newVal);
    }

    default void increase(int amount) {
        int newVal = this.getChaosValue() + amount;

        if (newVal > 1000 /*ServerConfig.MAX_CHAOS.get()*/)
            return;

        this.setChaosValue(newVal);
    }

}
