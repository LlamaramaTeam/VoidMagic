package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.common.util.IdHelper;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SpellbindingClothItem extends Item {

    public static final String SHINY_KEY = "item.voidmagic.spellbinding_cloth.shiny";
    // TODO More config stuff.
    /* public static final Set<Block> CLOTH_BLACKLIST =
            ServerConfig.SPELLBINDING_CLOTH_BLACKLIST.get().stream()
                    .map(IdHelper::getBlockFromID)
                    .map((optionalBlock) -> optionalBlock.orElse(Blocks.AIR))
                    .collect(Collectors.toSet());*/

    public SpellbindingClothItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();


        if (playerEntity == null || !playerEntity.isSneaking()) {
            return ActionResult.PASS;
        }

        // TODO More config stuff.
//        BlockState state = world.getBlockState(pos);
//        Block block = state.getBlock();

        // Make sure we are on the server for logic.
        if (!world.isClient/* && !CLOTH_BLACKLIST.contains(block)*/) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity == null)
                return ActionResult.PASS;

            // Generate the itemstack of packed blocks that would be appropriate for the target.
            ItemStack resultStack = this.getItemStackFromTarget(blockEntity, world, pos);
            // Make sure no items are left before we break.
            this.breakWithNoItemDrops(pos, world);
            ItemEntity toSpawn =
                    new ItemEntity(world, pos.getX() + 0.5d, pos.getY() + 0.5f, pos.getZ() + 0.5f, resultStack);

            if (world.spawnEntity(toSpawn)) {
                ItemStack activeStack = playerEntity.getStackInHand(context.getHand());
                Item heldItem = activeStack.getItem();
                if (heldItem instanceof SpellbindingClothItem)
                    activeStack.decrement(1);
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    /**
     * Used to make an {@link ItemStack} with the correct NBT tag for an item usage.
     *
     * @param blockEntity The target inventory.
     * @param world       The world that entity exists in.
     * @param pos         The position of that entity.
     * @return An {@link ItemStack} of {@link PackedBlockItem} that contains the correct tag.
     */
    public ItemStack getItemStackFromTarget(BlockEntity blockEntity, World world, BlockPos pos) {
        ItemStack stackOut = new ItemStack(ModItems.PACKED_BLOCK, 1);
        // The block that will later be written to the stack nbt.
        BlockState state = world.getBlockState(pos);

        /*
            Then we write the the id of the block that we recorded earlier.
         */
        stackOut.getOrCreateNbt().putString(NBTConstants.BLOCK_ID, IdHelper.getIdString(state.getBlock()));
        /*
            Then we write the extra data of that block entity.
         */
        stackOut.getOrCreateNbt().put(NBTConstants.EXTRA_NBT, blockEntity.writeNbt(new NbtCompound()));

        // Return the stack.
        return stackOut;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        TranslatableText shinyText = new TranslatableText(SHINY_KEY);
        shinyText.styled((style) -> style.withItalic(true).withFormatting(Formatting.AQUA));
        tooltip.add(shinyText);
    }

    private void breakWithNoItemDrops(BlockPos pos, World world) {
        Optional.ofNullable(world.getBlockEntity(pos)).ifPresent((blockEntity) -> {
            world.removeBlockEntity(pos);
            world.removeBlock(pos, false);
        });
    }

}
