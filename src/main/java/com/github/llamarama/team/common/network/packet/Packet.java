package com.github.llamarama.team.common.network.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface Packet {

    Identifier getId();

    PacketByteBuf getBuffer();

}
