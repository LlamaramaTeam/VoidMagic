package io.github.llamarama.team.voidmagic.common.block;

import io.github.llamarama.team.voidmagic.common.register.ModMultiblocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class TestBlock extends Block {

    public TestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && ModMultiblocks.RANDOM_TYPE.existsAt(pos.down(2), world)) {
            world.createExplosion(null,
                    pos.getX() + 0.5d,
                    pos.getY() + 0.5d,
                    pos.getZ() + 0.5d,
                    3f,
                    Explosion.DestructionType.NONE);
        }

        return ActionResult.SUCCESS;
    }

}
