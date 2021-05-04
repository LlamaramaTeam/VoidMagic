package io.github.llamarama.team.voidmagic.common.item;

import io.github.llamarama.team.voidmagic.common.register.ModItems;
import io.github.llamarama.team.voidmagic.common.util.IdHelper;
import io.github.llamarama.team.voidmagic.common.util.config.ServerConfig;
import io.github.llamarama.team.voidmagic.common.util.constants.NBTConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fancy shiny item, and stuff.
 *
 * @author 0xJoeMama
 * @since 2021
 */
public class SpellBindingClothItem extends Item {

    public static final String SHINY_KEY = "item.voidmagic.spellbinding_cloth.shiny";
    public static final Set<Block> CLOTH_BLACKLIST =
            ServerConfig.SPELLBINDING_CLOTH_BLACKLIST.get().stream()
                    .map(IdHelper::getBlockFromID)
                    .map((optionalBlock) -> optionalBlock.orElse(Blocks.AIR))
                    .collect(Collectors.toSet());

    public SpellBindingClothItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState state = world.getBlockState(pos);

        if (playerEntity == null || !playerEntity.isSneaking())
            return ActionResultType.PASS;
        Block block = state.getBlock();

        // Make sure we are on the server for logic.
        if (!world.isRemote() && !CLOTH_BLACKLIST.contains(block)) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity == null)
                return ActionResultType.PASS;

            // Generate the itemstack of packed blocks that would be good for the target.
            ItemStack resultStack = this.getItemStackFromTarget(tileEntity, world, pos);
            // Make sure no items are left before we break.
            this.breakWithNoItemDrops(pos, world);
            ItemEntity toSpawn =
                    new ItemEntity(world, pos.getX() + 0.5d, pos.getY() + 0.5f, pos.getZ() + 0.5f, resultStack);

            if (world.addEntity(toSpawn)) {
                ItemStack activeStack = playerEntity.getHeldItem(context.getHand());
                Item heldItem = activeStack.getItem();
                if (heldItem instanceof SpellBindingClothItem)
                    activeStack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    protected void breakWithNoItemDrops(BlockPos pos, World world) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity == null)
            return;

        world.removeTileEntity(pos);
        world.removeBlock(pos, false);
    }

    /**
     * Used to make an {@link ItemStack} with the correct NBT tag for an item usage.
     *
     * @param tileEntity The target inventory.
     * @param world      The world that entity exists in.
     * @param pos        The position of that entity.
     * @return An {@link ItemStack} of {@link PackedBlockItem} that contains the correct tag.
     */
    public ItemStack getItemStackFromTarget(TileEntity tileEntity, World world, BlockPos pos) {
        ItemStack stackOut = new ItemStack(ModItems.PACKED_BLOCK.get(), 1);
        // The block that will later be written to the stack nbt.
        BlockState state = world.getBlockState(pos);

        /*
            Then we write the the id of the block that we recorded earlier.
         */
        stackOut.getOrCreateTag().putString(NBTConstants.BLOCK_ID, IdHelper.getIdString(state.getBlock()));
        /*
            Then we write the extra data of that tile entity.
         */
        stackOut.getOrCreateTag().put(NBTConstants.EXTRA_NBT, tileEntity.write(new CompoundNBT()));

        // Return the stack.
        return stackOut;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        TranslationTextComponent textComponent = new TranslationTextComponent(SHINY_KEY);
        textComponent.modifyStyle((style) -> style.setItalic(true).setFormatting(TextFormatting.AQUA));
        tooltip.add(textComponent);
    }

}
