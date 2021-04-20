package io.github.llamarama.team.voidmagic.common.event;

import io.github.llamarama.team.voidmagic.common.capability.CapUtils;
import io.github.llamarama.team.voidmagic.common.capability.VoidMagicCaps;
import io.github.llamarama.team.voidmagic.common.capability.handler.IChaosHandler;
import io.github.llamarama.team.voidmagic.common.capability.provider.ChaosChunkProvider;
import io.github.llamarama.team.voidmagic.common.network.ModNetworking;
import io.github.llamarama.team.voidmagic.common.network.packet.ChunkChaosUpdatePacket;
import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.util.IdBuilder;
import io.github.llamarama.team.voidmagic.util.PlayerUtil;
import io.github.llamarama.team.voidmagic.util.config.ServerConfig;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
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

    /**
     * Give the book if that config option is enabled.
     *
     * @param event {@link PlayerEvent.PlayerRespawnEvent}
     */
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
        // TODO: Finish this because it's dangerous.
        Chunk object = event.getObject();
        ChaosChunkProvider provider = new ChaosChunkProvider(object);
        event.addCapability(IdBuilder.mod(NBTConstants.CHAOS), provider);
        event.addListener(provider::invalidate);
    }

    /**
     * Probably not the best solution. Will change.
     *
     * @param event {@link EntityEvent.EnteringChunk}
     */
    @SubscribeEvent
    public void onEnterChunk(final EntityEvent.EnteringChunk event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayerEntity && !entity.world.isRemote) {
            Chunk chunk = entity.world.getChunk(event.getNewChunkX(), event.getNewChunkZ());
            ModNetworking.get().sendToClient(new ChunkChaosUpdatePacket(chunk), (ServerPlayerEntity) entity);
        }
    }

    @SubscribeEvent
    public void onLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        // First we update the chunk the player is in so that he can she the actual chaos value.
        if (player instanceof ServerPlayerEntity) {
            Chunk chunkAt = player.world.getChunkAt(player.getPosition());

            CapUtils.executeForChaos(chunkAt, (chaosHandler) ->
                    ModNetworking.get().sendToClient(new ChunkChaosUpdatePacket(chunkAt), (ServerPlayerEntity) player)
            );
        }
    }

    /**
     * @param event Just testing
     *              Marked for removal
     * @Deprecated
     */
    @Deprecated
    @SubscribeEvent
    public void onHit(final LivingDamageEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        DamageSource source = event.getSource();

        Entity trueSource = source.getTrueSource();

        if (trueSource instanceof ServerPlayerEntity) {
            BlockPos position = entityLiving.getPosition();

            LazyOptional<IChaosHandler> capability =
                    entityLiving.getEntityWorld().getChunkAt(position).getCapability(VoidMagicCaps.CHAOS);

            capability.ifPresent((chaosHandler) ->
                    PlayerUtil.sendMessage(chaosHandler.getChaos(), ((ServerPlayerEntity) trueSource)));
        }
    }


}
