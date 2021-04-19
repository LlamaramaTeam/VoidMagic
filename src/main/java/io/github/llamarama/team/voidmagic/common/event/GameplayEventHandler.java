package io.github.llamarama.team.voidmagic.common.event;

import io.github.llamarama.team.voidmagic.common.capability.provider.ChaosChunkProvider;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.util.IdBuilder;
import io.github.llamarama.team.voidmagic.util.config.ServerConfig;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameplayEventHandler implements IEventHandler {

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

    /**
     * @param event nothing.
     * @Deprecated Test
     */
//    @SubscribeEvent
//    public void attachToEntity(final AttachCapabilitiesEvent<Entity> event) {
//        EntityType<?> type = event.getObject().getType();
//
//        if (type == EntityType.BLAZE) {
//            event.addCapability(IdBuilder.mod(NBTConstants.CHAOS.toLowerCase()), new ChaosEntityProvider());
//        }
//    }
    @SubscribeEvent
    public void attachCustomCaps(final AttachCapabilitiesEvent<Chunk> event) {
        // TODO: Finish this because it's dangerous.
        Chunk object = event.getObject();
        ChaosChunkProvider provider = new ChaosChunkProvider(object);
        event.addCapability(IdBuilder.mod(NBTConstants.CHAOS), provider);
        event.addListener(provider::invalidate);
    }

}
