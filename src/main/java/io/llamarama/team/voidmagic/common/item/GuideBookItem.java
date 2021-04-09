package io.llamarama.team.voidmagic.common.item;

import io.llamarama.team.voidmagic.client.guide.InitialGuideBookScreen;
import io.llamarama.team.voidmagic.common.network.packet.OpenScreenPacket;
import io.llamarama.team.voidmagic.common.network.ModNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class GuideBookItem extends Item {

    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity user, Hand hand) {
        ItemStack itemInHand = user.getHeldItem(hand);
        if (!world.isRemote) {
            ModNetworking.sendToClient(
                    new OpenScreenPacket(new InitialGuideBookScreen(itemInHand), "hallo"),
                    (ServerPlayerEntity) user);
            user.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
            return ActionResult.resultSuccess(itemInHand);
        }

        return ActionResult.resultSuccess(itemInHand);
    }

}
