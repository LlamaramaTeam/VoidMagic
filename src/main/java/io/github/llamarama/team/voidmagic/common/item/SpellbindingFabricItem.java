package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.register.ModBlocks;
import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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

        if (!world.isRemote()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity == null)
                return ActionResultType.PASS;


            this.spawnItemWithContents(tileEntity, world, pos);
        }
        return ActionResultType.SUCCESS;
    }

    public void spawnItemWithContents(TileEntity tileEntity, World world, BlockPos pos) {
        ItemStack stackOut = new ItemStack(ModBlocks.PACKED_BLOCK.get(), 1);

        if (tileEntity instanceof IInventory) {
            int size = ((IInventory) tileEntity).getSizeInventory();
            ListNBT containerItemTag = new ListNBT();

            for (int i = 0; i < size; i++) {
                ItemStack stackInSlot = ((IInventory) tileEntity).getStackInSlot(i);
                CompoundNBT currentStackToNBT = stackInSlot.write(new CompoundNBT());
                containerItemTag.add(i, currentStackToNBT);
            }

            VoidMagic.getLogger().debug(containerItemTag);

            CompoundNBT stackTag = stackOut.getOrCreateTag();
            stackTag.put(NBTConstants.INVENTORY, containerItemTag);
            System.out.println(stackTag);

            world.getPlayers().forEach((playerEntity) -> playerEntity.addItemStackToInventory(stackOut));
        }
    }

}
