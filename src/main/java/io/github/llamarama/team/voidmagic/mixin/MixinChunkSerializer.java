package io.github.llamarama.team.voidmagic.mixin;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.lib.chaos.ChaosProvider;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public abstract class MixinChunkSerializer {

    @Inject(method = "serialize", at = @At("RETURN"), cancellable = true)
    private static void serializeCustomData(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound voidMagicExtraData = new NbtCompound();

        voidMagicExtraData.putString(NBTConstants.VOIDMAGIC_VERSION_KEY, NBTConstants.VOIDMAGIC_VERSION);

        if (chunk instanceof ChaosProvider) {
            voidMagicExtraData.putInt(NBTConstants.CHAOS, ((ChaosProvider) chunk).getChaosValue());
        }

        NbtCompound returnValue = cir.getReturnValue();
        returnValue.getCompound("Level").put(NBTConstants.VOIDMAGIC_DATA, voidMagicExtraData);

        cir.setReturnValue(returnValue);
    }

    @Inject(method = "loadEntities", at = @At("RETURN"))
    private static void loadChaos(ServerWorld world, NbtCompound nbt, WorldChunk chunk, CallbackInfo ci) {
        if (chunk instanceof ChaosProvider) {
            NbtCompound voidMagicData = nbt.getCompound(NBTConstants.VOIDMAGIC_DATA);

            int chaosValue = voidMagicData.getInt(NBTConstants.CHAOS);
            VoidMagic.getLogger().info(chaosValue);

            ((ChaosProvider) chunk).setChaosValue(chaosValue);
        }
    }

}
