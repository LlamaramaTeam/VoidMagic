package io.github.llamarama.team.voidmagic.common.tile;

import io.github.llamarama.team.voidmagic.api.block.properties.ModBlockProperties;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblock;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockProvider;
import io.github.llamarama.team.voidmagic.api.multiblock.IMultiblockType;
import io.github.llamarama.team.voidmagic.api.spellbinding.ISpellbindingCircle;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.ModMultiblocks;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.Multiblock;
import io.github.llamarama.team.voidmagic.common.lib.multiblock.impl.MultiblockType;
import io.github.llamarama.team.voidmagic.common.register.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class ScrollTileEntity extends TileEntity implements ITickableTileEntity, IMultiblockProvider {

    private final IMultiblock multiblock;
    private int craftingTick;
    private boolean isCrafting;
    private ISpellbindingCircle currentCircle;

    public ScrollTileEntity() {
        super(ModTileEntityTypes.SCROLL.get());
        this.multiblock = new Multiblock(ModMultiblocks.FANCY, this.getPos());
    }

    @Override
    public void tick() {
        if (this.world == null)
            return;

        if (!this.world.isRemote()) {
            // Server logic

            // Check for correct position.
            BlockState currentState = this.getBlockState();
            if (currentState.get(ModBlockProperties.OPEN) && !currentState.isValidPosition(this.world, this.getPos()))
                this.world.destroyBlock(this.getPos(), true);

            // Start the circle logic.
            if (this.multiblock.exists(this.world)) {
                this.multiblock.positions().forEach(pos -> this.world.removeBlock(pos, false));
            }

            if (this.craftingTick == 0 && this.currentCircle == null) {
                Set<IMultiblockType> possibleTypes = MultiblockType.REGISTRY.keySet().stream()
                        .filter((type) -> type.existsAt(this.pos, this.world))
                        .collect(Collectors.toSet());

                Optional<IMultiblockType> finalType = possibleTypes.stream().reduce((type1, type2) -> {
                    Vector3i size1 = type1.getSize();
                    Vector3i size2 = type2.getSize();

                    boolean isXLarger = size1.getX() > size2.getX();
                    boolean isYLarger = size1.getY() > size2.getY();
                    boolean isZLarger = size1.getZ() > size2.getZ();

                    return isXLarger && isYLarger && isZLarger ? type1 :
                            !isXLarger && !isYLarger && !isZLarger ? type2 :
                                    ((BooleanSupplier) () -> {
                                        if (isXLarger && isZLarger)
                                            return true;
                                        else if (size1 == size2)
                                            return true;
                                        else if (isXLarger && isYLarger)
                                            return true;
                                        else return !isYLarger || isZLarger;
                                    }).getAsBoolean() ? type1 : type2;
                });
            }
        }

        // Maybe some client logic
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.multiblock.deserialize(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT tag = super.write(compound);
        this.multiblock.serialize(tag);
        this.multiblock.setPos(this.getPos());
        return tag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        Optional.ofNullable(this.world).ifPresent((world) -> {
            CompoundNBT tag = pkt.getNbtCompound();
            BlockPos pos = pkt.getPos();

            this.handleUpdateTag(world.getBlockState(pos), tag);
        });
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
    }

    @Override
    public IMultiblock getMultiblock() {
        return this.multiblock;
    }

}
