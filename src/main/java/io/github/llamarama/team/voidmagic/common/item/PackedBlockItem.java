package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PackedBlockItem extends BlockItem {

    public static final String CONTENT_KEY = "block.voidmagic.packed_block.contents";
    public static final String KEY = "block.voidmagic.packed_block";

    public PackedBlockItem(Settings settings) {
        super(Blocks.AIR, settings);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return new TranslatableText(KEY).getString();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        NbtCompound stackTag = stack.getOrCreateNbt();
        Block fromTag = this.getBlockFromTag(stackTag);

        // Get the name of the block in this stack amd put it in the tooltip.
        tooltip.add(new TranslatableText(CONTENT_KEY, fromTag.getName()));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    protected boolean place(ItemPlacementContext context, BlockState state) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        // Put logic on the server.
        if (!world.isClient) {
            if (player == null) return false;
            // Get the packed stack that will later be used for extracting the tag information.
            ItemStack packedBlockStack = player.getStackInHand(hand);

            // Place the block.
            boolean success = this.placeBaseBlock(packedBlockStack, world, pos, context);

            // Get the tag and extract the inventory.
            NbtCompound tag = packedBlockStack.getOrCreateNbt();
            NbtCompound tileNBT = tag.getCompound(NBTConstants.EXTRA_NBT);

            tileNBT.putInt(NBTConstants.X, pos.getX());
            tileNBT.putInt(NBTConstants.Y, pos.getY());
            tileNBT.putInt(NBTConstants.Z, pos.getZ());

            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null)
                blockEntity.readNbt(tileNBT);

            return success;
        }

        return true;
    }

    /**
     * Using the tag that was saved to the block, it places the target block.
     *
     * @param packedBlockStack The stack of {@link PackedBlockItem}
     * @param world            World the event happened in.
     * @param pos              The position it happened in.
     * @param context          The block placement context.
     * @return Whether the block was placed successfully.
     */
    private boolean placeBaseBlock(ItemStack packedBlockStack, World world, BlockPos pos, ItemPlacementContext context) {
        Block blockFromTag = this.getBlockFromTag(packedBlockStack.getOrCreateNbt());

        boolean result;
        Optional<BlockState> state = Optional.ofNullable(blockFromTag.getPlacementState(context));
        result = world.setBlockState(pos, state.orElseGet(blockFromTag::getDefaultState));

        return result;
    }

    /**
     * Get a block from the block registry, using the provided tag.
     *
     * @param stackTag A tag that contains a block id.
     * @return The block read from the tag OR Blocks.AIR.
     */
    private Block getBlockFromTag(NbtCompound stackTag) {
        String blockIdString = stackTag.getString(NBTConstants.BLOCK_ID);

        return Registry.BLOCK.get(new Identifier(blockIdString));
    }

}
