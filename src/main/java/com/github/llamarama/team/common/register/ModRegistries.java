package com.github.llamarama.team.common.register;

public final class ModRegistries {

    private ModRegistries() {

    }

    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModBlockEntityTypes.init();
    }

}
