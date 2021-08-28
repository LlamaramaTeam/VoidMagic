package io.github.llamarama.team.voidmagic.mixin;

import io.github.llamarama.team.voidmagic.common.util.ChaosUtils;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public abstract class MixinChunkSerializer {

    @Inject(method = "serialize", at = @At("RETURN"), cancellable = true)
    private static void serializeCustomData(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound voidMagicExtraData = new NbtCompound();

        voidMagicExtraData.putString(NBTConstants.VOIDMAGIC_VERSION_KEY, NBTConstants.VOIDMAGIC_VERSION);
        ChaosUtils.executeForChaos(chunk,
                chaosProvider -> voidMagicExtraData.putInt(NBTConstants.CHAOS, chaosProvider.getChaosValue())
        );

        NbtCompound returnValue = cir.getReturnValue();
        returnValue.put(NBTConstants.VOIDMAGIC_DATA, voidMagicExtraData);

        cir.setReturnValue(returnValue);
    }

    @ModifyVariable(method = "deserialize", at = @At(value = "STORE"))
    private static Chunk onChunk2Stored(Chunk chunk2, ServerWorld world, StructureManager structureManager,
                                        PointOfInterestStorage poiStorage, ChunkPos pos, NbtCompound nbt) {
        NbtCompound voidMagicData = nbt.getCompound(NBTConstants.VOIDMAGIC_DATA);

        ChaosUtils.executeForChaos(chunk2,
                (chaosProvider) -> chaosProvider.setChaosValue(voidMagicData.getInt(NBTConstants.CHAOS)));

        return chunk2;
    }

}
