package io.github.llamarama.team.voidmagic.common.lib.spellbinding;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum SpellbindingPhase {

    // Items are extracted from containers for now.
    INITIATING((currentTime, totalTime) ->
            totalTime * 2 / 3 < currentTime),
    // The beginning of the seal forming on the ground.
    BINDING((currentTime, totalTime) ->
            !INITIATING.timeCheck.apply(currentTime, totalTime) && currentTime >= 5 * totalTime / 6),
    // Consume the chalk and bind the scroll.
    FINALIZING((currentTime, totalTime) ->
            !BINDING.timeCheck.apply(currentTime, totalTime) &&
                    !INITIATING.timeCheck.apply(currentTime, totalTime) &&
                    currentTime < 5 * totalTime / 6);

    private final BiFunction<Integer, Integer, Boolean> timeCheck;

    SpellbindingPhase(BiFunction<Integer, Integer, Boolean> timeCheck) {
        this.timeCheck = timeCheck;
    }

    public static SpellbindingPhase get(int currentTick, int totalCircleTime) {
        return Arrays.stream(values())
                .filter((phase) -> phase.timeCheck.apply(currentTick, totalCircleTime))
                .findFirst().orElseThrow(() -> new RuntimeException("Invalid phase for time " + currentTick));
    }

}

