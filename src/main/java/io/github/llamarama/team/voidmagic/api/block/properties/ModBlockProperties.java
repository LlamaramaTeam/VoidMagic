package io.github.llamarama.team.voidmagic.api.block.properties;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public final class ModBlockProperties {

    public static final EnumProperty<ChalkType> CHALK_TYPE = EnumProperty.of("chalk_type", ChalkType.class);
    public static final BooleanProperty HAS_TOP = BooleanProperty.of("has_top");
    public static final BooleanProperty HAS_BOTTOM = BooleanProperty.of("has_bottom");
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    private ModBlockProperties() {

    }

}
