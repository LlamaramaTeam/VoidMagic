package io.github.llamarama.team.voidmagic.client.register;

import io.github.llamarama.team.voidmagic.client.render.renderer.tile.OfferingPlateTileRenderer;
import io.github.llamarama.team.voidmagic.client.render.renderer.tile.ScrollTileEntityRenderer;
import io.github.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.function.Function;

public class ModTileEntityRenderers {

    public static void init() {
        register(ModTileEntityTypes.OFFERING_PLATE, OfferingPlateTileRenderer::new);
        register(ModTileEntityTypes.SCROLL, ScrollTileEntityRenderer::new);
    }

    private static <T extends TileEntity> void register(RegistryObject<TileEntityType<T>> tileTypeRegObj,
                                                        Function<TileEntityRendererDispatcher, TileEntityRenderer<T>> factory) {
        ClientRegistry.bindTileEntityRenderer(tileTypeRegObj.get(), factory);
    }


}
