package io.llamarama.team.voidmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WitheredStoneBlock extends Block {

    public WitheredStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            Effect wither = Effects.WITHER;

            boolean doesntHave = livingEntity.getActivePotionEffects().stream().map(EffectInstance::getPotion).noneMatch(wither::equals);

            if (doesntHave) {
                EffectInstance instance = new EffectInstance(wither, 1, 5, false, false);
                livingEntity.addPotionEffect(instance);
            }
        }
    }

}
