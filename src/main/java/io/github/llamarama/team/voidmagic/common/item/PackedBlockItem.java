package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.VoidMagic;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
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
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * And there you were thinking that this was a block. Got'em!
 *
 * @author 0xJoeMama
 * @since 2021
 */
public class PackedBlockItem extends BlockItem {

    public static final String CONTENT_KEY = "block.voidmagic.packed_block.contents";
    public static final String CONTAINS_KEY = "block.voidmagic.packed_block.contains";
    public static final String KEY = "block.voidmagic.packed_block";

    public PackedBlockItem(Properties properties) {
        super(Blocks.WHITE_WOOL, properties);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return new TranslationTextComponent(KEY).getString();
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World world = context.getWorld();
        // Put logic on the server.
        if (!world.isRemote()) {
            if (player == null)
                return false;
            // Get the packed stack that will later be used for extracting the tag information.
            ItemStack packedBlockStack = player.getHeldItem(hand);

            // Place the block.
            boolean success = this.placeBaseBlock(packedBlockStack, world, context.getPos(), context);

            // Get the tag and extract the inventory.
            CompoundNBT tag = packedBlockStack.getOrCreateTag();
            ListNBT inventoryTag = (ListNBT) tag.get(NBTConstants.INVENTORY);

            TileEntity tileEntity = world.getTileEntity(context.getPos());
            // Insert all the items from the tag.
            if (inventoryTag != null && tileEntity != null)
                this.insertItemsToInventory(tileEntity, inventoryTag);

            return success;
        }

        return true;
    }

    /**
     * Using the list tag, it insert all the content back to the inventory.
     *
     * @param tileEntity   The inventory.
     * @param inventoryTag The tah with all the contents.
     */
    private void insertItemsToInventory(TileEntity tileEntity, ListNBT inventoryTag) {
        // Lambda magic to prevent checking 5 gjillion times.
        // Currently may cause AIOOB exception.
        BiConsumer<Integer, ItemStack> convertInsertItem;
        final AtomicInteger inventorySize = new AtomicInteger(0);
        if (tileEntity instanceof IInventory) {
            convertInsertItem = ((IInventory) tileEntity)::setInventorySlotContents;
            inventorySize.set(((IInventory) tileEntity).getSizeInventory());
        } else {
            LazyOptional<IItemHandler> lazyHandler =
                    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

            // Lambda to lambda. Lel?!
            // At least it is not () -> () ->
            convertInsertItem = (integer, itemStack) -> lazyHandler.ifPresent((itemHandler) -> {
                itemHandler.insertItem(integer, itemStack, false);
                inventorySize.set(itemHandler.getSlots());
            });
        }

        VoidMagic.getLogger().debug(inventorySize.get());
        for (int i = 0; i < inventorySize.get(); i++) {
            CompoundNBT currentStack = inventoryTag.getCompound(i);
            ItemStack currentSlot = ItemStack.read(currentStack);

            convertInsertItem.accept(i, currentSlot);
        }
    }

    /**
     * Using the tag that was saved to the block, it places the target block.
     *
     * @param stack   The stack of {@link PackedBlockItem}
     * @param world   World the event happened in.
     * @param pos     The position it happened in.
     * @param context The block placement context.
     * @return Whether the block was placed successfully.
     */
    protected boolean placeBaseBlock(ItemStack stack, World world, BlockPos pos, BlockItemUseContext context) {
        Optional<Block> blockFromTag = this.getBlockFromTag(stack.getOrCreateTag());

        /*
            I didn't want to use atomic reference here but whatever.
         */
        final AtomicBoolean result = new AtomicBoolean();
        blockFromTag.ifPresent((block) -> {
            Optional<BlockState> state = Optional.ofNullable(block.getStateForPlacement(context));
            result.set(world.setBlockState(pos, state.orElseGet(block::getDefaultState)));
        });

        return result.get();
    }

    /**
     * Get a block from the {@link ForgeRegistries#BLOCKS} registry, using the provided tag.
     *
     * @param tag A tag that contains a block id.
     * @return An {@link Optional} that may contain the block with the id from the provided tag.
     */
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
                The only reason it was written is for this, so that we wont have to check every tick for the tooltip.
             */
            int filledStacks = stackTag.getInt(NBTConstants.FILLED_STACKS);
            tooltip.add(new TranslationTextComponent(CONTAINS_KEY,
                    new StringTextComponent(Integer.toString(filledStacks))));
        });
    }

}
