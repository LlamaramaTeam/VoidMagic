package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellbindingFabricItem extends Item {

    public SpellbindingFabricItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (!world.isRemote()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity == null)
                return ActionResultType.PASS;


            this.spawnItemWithContents(tileEntity, world, pos);
        }
        return ActionResultType.SUCCESS;
    }

    public void spawnItemWithContents(TileEntity tileEntity, World world, BlockPos pos) {
//        ItemEntity itemEntity =
//                new ItemEntity(world, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);

        ItemStack stack = world.getBlockState(pos).getBlock().asItem().getDefaultInstance();

        if (tileEntity instanceof IInventory) {
            int size = ((IInventory) tileEntity).getSizeInventory();
            CompoundNBT tag = stack.getOrCreateChildTag(NBTConstants.INVENTORY);

            for (int i = 0; i < size; i++) {
                ItemStack stackInSlot = ((IInventory) tileEntity).getStackInSlot(i);
                stackInSlot.write(tag);
            }
            VoidMagic.getLogger().debug(tag.toString());
        }
    }

}
