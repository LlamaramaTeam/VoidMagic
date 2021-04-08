package io.llamarama.team.voidmagic.util.constants;

import io.llamarama.team.voidmagic.util.IdBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public enum StringConstants {

    EMPTY(() -> ""),
    MOD_ID(() -> StringConstants.MOD_ID_STR),
    MOD_VERSION(() -> "1.0.0"),
    CURIOS_ID(() -> "curios"),
    NETWORK_PROTOCOL_VERSION(MOD_VERSION::get);

    private final Lazy<String> value;
    public static final String MOD_ID_STR = "voidmagic";

    StringConstants(Supplier<String> value) {
        this.value = Lazy.of(value);
    }

    public String get() {
        return this.value.get();
    }

    public enum Network {
        CHANNEL_ID("voidmagic_net");

        private final ResourceLocation id;

        Network(String id) {
            this.id = IdBuilder.mod(id);
        }

        public ResourceLocation getId() {
            return this.id;
        }
    }

}
