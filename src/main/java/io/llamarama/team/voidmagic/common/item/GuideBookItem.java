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
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity user, Hand hand) {
        ItemStack itemInHand = user.getHeldItem(hand);
        if (world.isRemote) {
            user.openContainer(null);
            return ActionResult.resultSuccess(itemInHand);
        }

        return ActionResult.resultSuccess(itemInHand);
    }

}
