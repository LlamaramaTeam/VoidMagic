package io.llamarama.team.voidmagic;

import io.llamarama.team.voidmagic.client.VoidMagicClient;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(VoidMagic.MODID)
public class VoidMagic {

    public static final String MODID = "voidmagic";
    public static final ItemGroup GROUP = new ModItemGroup("voidmagic.group");
    private static final Logger LOGGER = LogManager.getLogger("Void Magic");

    public VoidMagic() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> VoidMagicClient::new);

        ModRegistries.initRegistries(modBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Logger getLogger() {
        return LOGGER;
    }


    private static final class ModItemGroup extends ItemGroup {

        public ModItemGroup(String label) {
            super(label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.PILLAGER_SPAWN_EGG);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }

    }

}
