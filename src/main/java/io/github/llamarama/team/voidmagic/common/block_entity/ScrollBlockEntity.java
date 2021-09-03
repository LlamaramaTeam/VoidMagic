package io.github.llamarama.team.voidmagic.common.block_entity;

import io.github.llamarama.team.voidmagic.api.spellbinding.CircleCaster;
import io.github.llamarama.team.voidmagic.api.spellbinding.SpellbindingCircle;
import io.github.llamarama.team.voidmagic.common.lib.spellbinding.CircleRegistry;
import io.github.llamarama.team.voidmagic.common.register.ModBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ScrollBlockEntity extends BlockEntity implements CircleCaster, BlockEntityClientSerializable {

    private int craftingTick;
    @Nullable
    private SpellbindingCircle currentCircle;

    public ScrollBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SCROLL, pos, state);
        this.craftingTick = 0;
        this.currentCircle = null;
    }

    public int getCraftingTick() {
        return this.craftingTick;
    }

    @Override
    public SpellbindingCircle getCircle() {
        return null;
    }

    public void cancelCrafting() {
        this.currentCircle = null;
        this.craftingTick = 0;
    }

    @Override
    public void fromClientTag(NbtCompound tag) {

    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return null;
    }

    public boolean isCrafting() {
        return this.craftingTick > 0 && this.currentCircle != null;
    }

    public void findCircle() {
        Set<SpellbindingCircle> possibleCircles = CircleRegistry.getRegistry().keySet().stream()
                .filter(spellbindingCircle -> spellbindingCircle.getMultiblockType().existsAt(this.pos, this.world))
                .collect(Collectors.toSet());

        Optional<SpellbindingCircle> finalType = possibleCircles.stream()
                .reduce((circle1, circle2) -> {
                    Vec3i size1 = circle1.getMultiblockType().getSize();
                    Vec3i size2 = circle2.getMultiblockType().getSize();

                    boolean isXLarger = size1.getX() > size2.getX();
                    boolean isYLarger = size1.getY() > size2.getY();
                    boolean isZLarger = size1.getZ() > size2.getZ();

                    return isXLarger && isYLarger && isZLarger ? circle1 :
                            !isXLarger && !isYLarger && !isZLarger ? circle2 : circle1;
                });

        finalType.ifPresent(this::initiateCircle);
    }

    public void progressCrafting() {
        this.craftingTick--;
    }

    public void initiateCircle(SpellbindingCircle circle) {
        this.craftingTick = circle.getCastingTime();
        this.currentCircle = circle;
    }

    public void finishCrafting() {
        if (this.currentCircle != null) {
            this.currentCircle.getResult().circleFormed(this.world, this.pos, this.getCachedState(),
                    this.currentCircle.multiblock(this.pos, this.world).get());
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

}
