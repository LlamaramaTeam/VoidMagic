package com.github.llamarama.team.common.register;

import com.github.llamarama.team.common.util.IdBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ModBlockEntityTypes {

    private static final Map<String, BlockEntityType<? extends BlockEntity>> REGISTRY = new ConcurrentHashMap<>();

    private ModBlockEntityTypes() {
    }

    @NotNull
    private static <BE extends BlockEntity> BlockEntityType<BE> register(String id,
                                                                         FabricBlockEntityTypeBuilder.Factory<BE> factory,
                                                                         Block... targetBlocks) {
        BlockEntityType<BE> type = FabricBlockEntityTypeBuilder.create(factory, targetBlocks).build();
        REGISTRY.putIfAbsent(id, type);
        return type;
    }

    static void init() {
        REGISTRY.forEach((id, type) -> Registry.register(Registry.BLOCK_ENTITY_TYPE, IdBuilder.of(id), type));
    }

}
