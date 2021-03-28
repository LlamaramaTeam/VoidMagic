package io.llamarama.team.voidmagic.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class GuideBookItem extends Item {

    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemInHand = user.getItemInHand(hand);
        if (world.isClientSide) {
            user.openMenu(null);
            return ActionResult.success(itemInHand);
        }

        return ActionResult.success(itemInHand);
    }

}
