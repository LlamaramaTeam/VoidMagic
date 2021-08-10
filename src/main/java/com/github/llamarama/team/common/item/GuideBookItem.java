package com.github.llamarama.team.common.item;

import com.github.llamarama.team.common.network.ModNetworking;
import com.github.llamarama.team.common.network.packet.OpenGuideBookPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GuideBookItem extends Item {

    public GuideBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemInHand = user.getStackInHand(hand);
        if (!world.isClient) {
            ModNetworking.get().sendToClient(new OpenGuideBookPacket(itemInHand), (ServerPlayerEntity) user);
            user.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
        }

        return TypedActionResult.success(itemInHand);
    }

}
