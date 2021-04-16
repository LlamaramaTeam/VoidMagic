package io.github.llamarama.team.voidmagic.common.event;

import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.util.config.ServerConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameplayEventHandler {

    private static GameplayEventHandler instance;

    private GameplayEventHandler() {
    }

    public static GameplayEventHandler getInstance() {
        if (instance == null) {
            instance = new GameplayEventHandler();
        }

        return instance;
    }

    @SubscribeEvent
    public void onPlayerSpawn(final PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player.world.isRemote) {
            return;
        }

        int deaths = ((ServerPlayerEntity) player).getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
        int minutePlayed = ((ServerPlayerEntity) player).getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_ONE_MINUTE));

        if (deaths == 0 && minutePlayed == 0 && ServerConfig.GIVE_BOOK_ON_START.get()) {
            player.addItemStackToInventory(new ItemStack(ModItems.GUIDE_BOOK.get()));
        }
    }

    @SubscribeEvent
    public void attachCustomCaps(final AttachCapabilitiesEvent<Chunk> event) {
        // TDOD: Finish this because it's dangerous.
        // Do NOT uncomment this.
//        event.addCapability(IdBuilder.mod(NBTConstants.CHAOS), new ChaosProvider());
//        event.addListener(ChaosProvider::invalidate);
    }

    public void registerHandlers(IEventBus forgeBus) {
        forgeBus.register(this);
    }

}
