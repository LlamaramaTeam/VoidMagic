package io.github.llamarama.team.voidmagic.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public final class PlayerUtil {

    public static void sendMessage(Object obj, PlayerEntity playerEntity) {
        playerEntity.sendStatusMessage(new StringTextComponent(obj.toString()), true);
    }

}
