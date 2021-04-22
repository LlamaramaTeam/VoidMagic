package io.github.llamarama.team.voidmagic.common.event;

import io.github.llamarama.team.voidmagic.common.capability.provider.ChaosChunkProvider;
import io.github.llamarama.team.voidmagic.common.misc.ChunkSyncManager;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.util.IdBuilder;
import io.github.llamarama.team.voidmagic.util.config.ServerConfig;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

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
    public void onServerStart(final FMLServerStartingEvent event) {
        // Create a chunk sync manager for the server.
        ChunkSyncManager.INSTANCE = new ChunkSyncManager();
    }

    @SubscribeEvent
    public void chunkWatched(final ChunkWatchEvent.Watch event) {
        ChunkPos pos = event.getPos();

        if (event.getWorld().isAreaLoaded(new BlockPos(pos.x * 16, 0, pos.z * 16), 1)) {
            ChunkSyncManager.INSTANCE.push(pos, event.getPlayer());
        }
    }

    @SubscribeEvent
    public void chunkUnwatched(final ChunkWatchEvent.UnWatch event) {
        ChunkPos pos = event.getPos();

        ChunkSyncManager.INSTANCE.pop(event.getPlayer(), pos);
    }

    @SubscribeEvent
    public void attachCustomCaps(final AttachCapabilitiesEvent<Chunk> event) {
        // TODO: Finish this because it's dangerous.
        Chunk object = event.getObject();
        ChaosChunkProvider provider = new ChaosChunkProvider(object);
        event.addCapability(IdBuilder.mod(NBTConstants.CHAOS), provider);
        event.addListener(provider::invalidate);
    }

    @SubscribeEvent
    public void onWorldTick(final TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (!event.world.isRemote && event.phase == TickEvent.Phase.START) {
            ChunkSyncManager.INSTANCE.enqueue(() -> {
                world.getPlayers().forEach((playerEntity) -> {
                    if (playerEntity instanceof ServerPlayerEntity)
                        ChunkSyncManager.INSTANCE.sendStatus((ServerPlayerEntity) playerEntity);
                });

                ChunkSyncManager.INSTANCE.popAll();
            });
        }
    }

    @SubscribeEvent
    public void onDisconnect(final PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();

        ChunkSyncManager.INSTANCE.popKey(player.getUniqueID());
    }

    @SubscribeEvent
    public void onLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player instanceof ServerPlayerEntity) {
            // Give the book if that config option is enabled.
            int deaths = ((ServerPlayerEntity) player).getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
            int minutePlayed = ((ServerPlayerEntity) player).getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_ONE_MINUTE));

            if (deaths == 0 && minutePlayed == 0 && ServerConfig.GIVE_BOOK_ON_START.get()) {
                player.addItemStackToInventory(new ItemStack(ModItems.GUIDE_BOOK.get()));
            }

        }
    }

    @SubscribeEvent
    public void onServerClose(final FMLServerStoppingEvent event) {
        // Remove the chunk sync manager from memory to prevent unecessary garbage collection.
        ChunkSyncManager.INSTANCE.popAll();
    }

    @SubscribeEvent
    public void onLivingHurt(final LivingHurtEvent event) {
        DamageSource source = event.getSource();

        if (event.getEntity().world.isRemote)
            return;

        if (source.getTrueSource() instanceof PlayerEntity)
            ChunkSyncManager.INSTANCE.sendStatus((ServerPlayerEntity) source.getTrueSource());

    }

}
