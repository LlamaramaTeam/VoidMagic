package io.llamarama.team.voidmagic.common.network;

import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworking {

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(StringConstants.Network.CHANNEL_ID.getId(),
            StringConstants.NETWORK_PROTOCOL_VERSION::toString,
            StringConstants.NETWORK_PROTOCOL_VERSION.get()::equals,
            StringConstants.NETWORK_PROTOCOL_VERSION.get()::equals);

}
