package io.llamarama.team.voidmagic;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@SuppressWarnings("unused")
public class ModMixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("voidmagic.mixins.json");
    }

}
