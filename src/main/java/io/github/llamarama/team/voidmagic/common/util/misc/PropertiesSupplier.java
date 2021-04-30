package io.github.llamarama.team.voidmagic.common.util.misc;

import net.minecraft.block.AbstractBlock;

import java.util.function.Supplier;

@FunctionalInterface
public interface PropertiesSupplier extends Supplier<AbstractBlock.Properties> {

    @Override
    AbstractBlock.Properties get();

}
