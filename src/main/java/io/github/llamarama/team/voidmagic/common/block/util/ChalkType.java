package io.github.llamarama.team.voidmagic.common.block.util;

import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

public enum ChalkType implements IStringSerializable {

    NONE("none", 0),
    LIGHT("light", 15),
    DARK("dark", 0),
    BALANCED("balanced", 5),
    UNSTABLE("unstable", 10),
    EARTH("earth", 4),
    WATER("water", 5),
    FIRE("fire", 6),
    AIR("air", 7);

    private final String string;
    private final int lightLevel;

    ChalkType(String string, int lightLevel) {
        this.string = string;
        this.lightLevel = lightLevel;
    }

    @NotNull
    @Override
    public String getString() {
        return this.string;
    }

    public int getLightLevel() {
        return this.lightLevel;
    }

    public ChalkType next() {
        int ordinal = this.ordinal();

        if (ordinal == ChalkType.values().length - 1) {
            return ChalkType.NONE;
        }

        for (ChalkType type : ChalkType.values()) {
            if (type.ordinal() == ordinal + 1) {
                return type;
            }
        }

        throw new IllegalStateException("Could not get valid next property for " + this.getString());
    }

}

