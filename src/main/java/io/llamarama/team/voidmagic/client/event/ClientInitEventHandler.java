package io.llamarama.team.voidmagic.client.event;

import io.llamarama.team.voidmagic.client.render.renderer.tile.OfferingPlateTileRenderer;
import io.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import io.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

public final class ClientInitEventHandler {

    private static ClientInitEventHandler instance;

    public static ClientInitEventHandler getInstance() {
        if (instance == null) {
            instance = new ClientInitEventHandler();
        }

        return instance;
    }

    public void clientInit(final FMLClientSetupEvent event) {
        registerTileRenderer(ModTileEntityTypes.OFFERING_PLATE, OfferingPlateTileRenderer::new);
    }

    private void registerTileRenderer(RegistryObject<TileEntityType<OfferingPlateTileEntity>> tile, Function<TileEntityRendererDispatcher, TileEntityRenderer<? super OfferingPlateTileEntity>> renderer) {
        ClientRegistry.bindTileEntityRenderer(tile.get(), renderer);
    }

    public void registerHandlers(IEventBus modBus) {
        modBus.addListener(this::clientInit);
    }

}
