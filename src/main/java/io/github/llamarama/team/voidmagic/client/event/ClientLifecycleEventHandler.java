package io.github.llamarama.team.voidmagic.client.event;

import io.github.llamarama.team.voidmagic.client.render.renderer.tile.OfferingPlateTileRenderer;
import io.github.llamarama.team.voidmagic.client.render.renderer.tile.ScrollTileEntityRenderer;
import io.github.llamarama.team.voidmagic.common.event.IEventHandler;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

public final class ClientLifecycleEventHandler implements IEventHandler {

    private static ClientLifecycleEventHandler instance;

    public static ClientLifecycleEventHandler getInstance() {
        if (instance == null) {
            instance = new ClientLifecycleEventHandler();
        }

        return instance;
    }

    @SubscribeEvent
    public void clientInit(final FMLClientSetupEvent event) {
        this.registerTileRenderer(ModTileEntityTypes.OFFERING_PLATE, OfferingPlateTileRenderer::new);
        this.registerTileRenderer(ModTileEntityTypes.SCROLL, ScrollTileEntityRenderer::new);

        // Initialize custom render types.
        this.setupRenderTypes();
    }

    /**
     * Setup custom {@link net.minecraft.block.Block}, {@link RenderType}s.
     *
     * @see RenderTypeLookup
     */
    private void setupRenderTypes() {
        RenderTypeLookup.setRenderLayer(ModBlocks.CHALK.get(), RenderType.getCutoutMipped());
    }

    private <T extends TileEntity> void registerTileRenderer(RegistryObject<TileEntityType<T>> tile, Function<TileEntityRendererDispatcher, TileEntityRenderer<T>> renderer) {
        ClientRegistry.bindTileEntityRenderer(tile.get(), renderer);
    }

}
