package io.llamarama.team.voidmagic.datagen.provider.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {

    private final Set<Block> blacklist;

    public ModLootTableProvider(DataGenerator gen) {
        super(gen);
        this.blacklist = Sets.newHashSet();
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((resourceLocation, lootTable) -> LootTableManager.validateLootTable(validationtracker, resourceLocation, lootTable));
    }

    private final class ModBlockLootTables extends BlockLootTables {

        @Override
        protected void addTables() {
            ModRegistries.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(ModLootTableProvider.this.blacklist::add)
                    .forEach(this::registerDropSelfLootTable);

            blacklist.forEach((block) -> {

            });
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModRegistries.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }

    }

}
