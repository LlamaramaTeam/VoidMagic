package com.github.llamarama.team.api.block.properties;

import net.minecraft.state.property.EnumProperty;

public final class ModBlockProperties {

    public static final EnumProperty<ChalkType> CHALK_TYPE = EnumProperty.of("chalk_type", ChalkType.class);

    private ModBlockProperties() {

    }

}
