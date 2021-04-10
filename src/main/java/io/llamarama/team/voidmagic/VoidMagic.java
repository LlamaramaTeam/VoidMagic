package io.llamarama.team.voidmagic;

import io.llamarama.team.voidmagic.client.VoidMagicClient;
import io.llamarama.team.voidmagic.common.event.CommonInitEventHandler;
import io.llamarama.team.voidmagic.common.register.ModItems;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import io.llamarama.team.voidmagic.util.IdBuilder;
import io.llamarama.team.voidmagic.util.constants.StringConstants;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(StringConstants.MOD_ID_STR)
public class VoidMagic {

    public static final String MOD_ID = StringConstants.MOD_ID.get();
    public static final ItemGroup GROUP = new ModItemGroup("voidmagic.group")
            .setBackgroundImage(IdBuilder.mod("textures/creative_tab.png"));
    private static final Logger LOGGER = LogManager.getLogger("Void Magic");

    public VoidMagic() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        CommonInitEventHandler.getInstance().registerHandlers(modBus);
        ModRegistries.initRegistries(modBus);

        // Initialize the client side of the mod.
        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> VoidMagicClient::new);

        // Register the mod to the forge event bus.
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        forgeBus.register(this);
    }

    public static Logger getLogger() {
        return LOGGER;
    }


    private static final class ModItemGroup extends ItemGroup {

        ModItemGroup(String label) {
            super(label);
        }

        @NotNull
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.GUIDE_BOOK.get());
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }

    }

}
