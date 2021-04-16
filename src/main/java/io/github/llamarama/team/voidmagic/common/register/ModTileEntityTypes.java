package io.github.llamarama.team.voidmagic.common.register;

import io.github.llamarama.team.voidmagic.common.tile.OfferingPlateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public final class ModTileEntityTypes {

    public static final RegistryObject<TileEntityType<OfferingPlateTileEntity>> OFFERING_PLATE = register("offering_plate",
            () -> TileEntityType.Builder.create(OfferingPlateTileEntity::new, ModBlocks.OFFERING_PLATE.get()).build(null));

    private ModTileEntityTypes() {
    }

    private static <TE extends TileEntity> RegistryObject<TileEntityType<TE>> register(String id, Supplier<? extends TileEntityType<TE>> supplier) {
        return ModRegistries.TILE_ENTITIES.register(id, supplier);
    }

    public static void init(IEventBus bus) {
        ModRegistries.TILE_ENTITIES.register(bus);
    }

}
