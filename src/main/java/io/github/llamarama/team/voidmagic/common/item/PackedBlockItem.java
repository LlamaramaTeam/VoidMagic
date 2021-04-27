package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.util.constants.NBTConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class PackedBlockItem extends BlockItem {

    public static final String CONTENT_KEY = "block.voidmagic.packed_block.contents";
    public static final String CONTAINS_KEY = "block.voidmagic.packed_block.contains";

    public PackedBlockItem(Properties properties) {
        super(Blocks.WHITE_WOOL, properties);
    }

    @Override
    public String getTranslationKey() {
        return new TranslationTextComponent("block.voidmagic.packed_block").getKey();
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World world = context.getWorld();
        if (!world.isRemote()) {
            if (player == null)
                return false;
            ItemStack packedBlockStack = player.getHeldItem(hand);

            boolean success = this.getBlockForPlacement(packedBlockStack, world, context.getPos());

            CompoundNBT tag = packedBlockStack.getOrCreateTag();
            ListNBT inventoryTag = (ListNBT) tag.get(NBTConstants.INVENTORY);

            if (inventoryTag != null) {
                TileEntity tileEntity = world.getTileEntity(context.getPos());
                if (tileEntity != null)
                    this.insertItemToInventory(tileEntity, inventoryTag);
            }
            return success;
        }

        return true;
    }

    private void insertItemToInventory(TileEntity tileEntity, ListNBT inventoryTag) {
        for (int i = 0; i < inventoryTag.size(); i++) {
            INBT inbt = inventoryTag.get(i);
            ItemStack currentSlot = ItemStack.read((CompoundNBT) inbt);

            if (tileEntity instanceof IInventory)
                ((IInventory) tileEntity).setInventorySlotContents(i, currentSlot);
            else {
                int finalI = i;
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                        .ifPresent(itemHandler -> itemHandler.insertItem(finalI, currentSlot, false));
            }
        }
    }

    protected boolean getBlockForPlacement(ItemStack stack, World world, BlockPos pos) {
        Optional<Block> blockFromTag = this.getBlockFromTag(stack.getOrCreateTag());

        final AtomicBoolean result = new AtomicBoolean();
        blockFromTag.ifPresent((block) -> result.set(world.setBlockState(pos, block.getDefaultState())));

        return result.get();
    }

    public Optional<Block> getBlockFromTag(CompoundNBT tag) {
        String blockIdString = tag.getString(NBTConstants.BLOCK_ID);
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockIdString));

        // We have to use optional here because the block is nullable.
        return Optional.ofNullable(block);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT stackTag = stack.getOrCreateTag();
        Optional<Block> fromTag = this.getBlockFromTag(stackTag);

        fromTag.ifPresent((block) -> {
            // Get the name of the block in this stack amd put it in the tooltip.
            tooltip.add(new TranslationTextComponent(CONTENT_KEY, block.getTranslatedName()));

            /*
                Get the amount of stacks that have items in the inventory.
                This number was written in the nbt tag of the stack when it got created.
             */
            int filledStacks = stackTag.getInt(NBTConstants.FILLED_STACKS);
            tooltip.add(new TranslationTextComponent(CONTAINS_KEY,
                    new StringTextComponent(Integer.toString(filledStacks))));
        });
    }

}
