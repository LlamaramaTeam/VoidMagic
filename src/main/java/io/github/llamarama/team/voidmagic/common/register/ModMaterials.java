package io.github.llamarama.team.voidmagic.common.register;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;

public final class ModMaterials {

    public static final Material SCROLL =
            new Material(MaterialColor.WOOL,
                    false, // isn't a fluid
                    false, // isn't solid
                    true, // blocks movement
                    true, // is opaque
                    true,  // is flammable
                    false, // can be replaced by grass/snow/etc
                    PushReaction.DESTROY); // is broken by pistons

    private ModMaterials() {
    }

}
