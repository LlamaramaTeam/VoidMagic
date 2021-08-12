package io.github.llamarama.team.voidmagic.mixin;

import io.github.llamarama.team.voidmagic.common.lib.chaos.ChaosProvider;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldChunk.class)
public abstract class MixinWorldChunk implements ChaosProvider {

    private int chaos;

    @Shadow
    public abstract void markDirty();

//    @Inject(
//            method = "<init>" +
//                    "(Lnet/minecraft/server/world/ServerWorld;" +
//                    "Lnet/minecraft/world/chunk/ProtoChunk;" +
//                    "Ljava/util/function/Consumer;)V",
//            at = @At("TAIL")
//
//    )
//    private void initiateCustomChaosValue(ServerWorld world, ProtoChunk protoChunk, Consumer<WorldChunk> consumer, CallbackInfo ci) {
//        if (protoChunk instanceof ChaosProvider)
//            this.chaos = ((ChaosProvider) protoChunk).getChaosValue();
//
//
//        System.out.println(protoChunk instanceof ChaosProvider);
//    }

    @Override
    public int getChaosValue() {
        return this.chaos;
    }

    @Override
    public void setChaosValue(int newVal) {
        this.chaos = newVal;
        this.markDirty();
    }

}
