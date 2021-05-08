package io.github.llamarama.team.voidmagic.client.render.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.llamarama.team.voidmagic.common.multiblock.impl.MultiblockType;

public class MultiblockRenderer {

    public void render(MultiblockType<?> type, MatrixStack matrixStack, IVertexBuilder vertexBuilder) {
        type.getKeys().forEach((pos, predicate) -> {

        });
    }

}
