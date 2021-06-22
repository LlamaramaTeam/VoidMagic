package io.github.llamarama.team.voidmagic.client.event;

import io.github.llamarama.team.voidmagic.client.misc.CircleTextureManager;
import io.github.llamarama.team.voidmagic.client.register.ModTileEntityRenderers;
import io.github.llamarama.team.voidmagic.client.render.renderer.tile.ScrollTileEntityRenderer;
import io.github.llamarama.team.voidmagic.common.event.IEventHandler;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientLifecycleEventHandler implements IEventHandler {

    private static ClientLifecycleEventHandler INSTANCE;

    public static ClientLifecycleEventHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientLifecycleEventHandler();
        }

        return INSTANCE;
    }

    @SubscribeEvent
    public void clientInit(final FMLClientSetupEvent event) {
        // Setup the TileEntityRenderers the mod uses.
        ModTileEntityRenderers.init();
        // Initialize custom render types.
        this.setupRenderTypes();

        CircleTextureManager.init();
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void onPreStitch(final TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            event.addSprite(ScrollTileEntityRenderer.SCROLL_PAGE);
        }
    }

    /**
     * Setup custom {@link net.minecraft.block.Block}, {@link RenderType}s.
     *
     * @see RenderTypeLookup
     */
    private void setupRenderTypes() {
        RenderTypeLookup.setRenderLayer(ModBlocks.CHALK.get(), RenderType.getCutoutMipped());
    }

}
