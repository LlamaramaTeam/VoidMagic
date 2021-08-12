package io.github.llamarama.team.voidmagic.common.register;

public final class ModRegistries {

    private ModRegistries() {

    }

    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModBlockEntityTypes.init();
    }

}
