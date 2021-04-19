package io.github.llamarama.team.voidmagic.client.event;

import io.github.llamarama.team.voidmagic.client.render.renderer.tile.OfferingPlateTileRenderer;
import io.github.llamarama.team.voidmagic.common.event.IEventHandler;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import io.github.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

public final class ClientInitEventHandler implements IEventHandler {

    private static ClientInitEventHandler instance;

    public static ClientInitEventHandler getInstance() {
        if (instance == null) {
            instance = new ClientInitEventHandler();
        }

        return instance;
    }

    @SubscribeEvent
    public void clientInit(final FMLClientSetupEvent event) {
        registerTileRenderer(ModTileEntityTypes.OFFERING_PLATE, OfferingPlateTileRenderer::new);

        // Initialize custom type.
        this.setupRenderTypes();
    }

    /**
     * Setup custom {@link net.minecraft.block.Block}, {@link RenderType}s.
     *
     * @see RenderTypeLookup
     */
    public void setupRenderTypes() {
        RenderTypeLookup.setRenderLayer(ModBlocks.CHALK.get(), RenderType.getCutoutMipped());
    }

    private void registerTileRenderer(RegistryObject<TileEntityType<OfferingPlateTileEntity>> tile, Function<TileEntityRendererDispatcher, TileEntityRenderer<? super OfferingPlateTileEntity>> renderer) {
        ClientRegistry.bindTileEntityRenderer(tile.get(), renderer);
    }

}
