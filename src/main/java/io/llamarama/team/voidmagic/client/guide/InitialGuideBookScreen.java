package io.llamarama.team.voidmagic.client.guide;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.llamarama.team.voidmagic.client.VoidMagicClient;
import io.llamarama.team.voidmagic.common.register.ModRegistries;
import io.llamarama.team.voidmagic.util.IdBuilder;
import io.llamarama.team.voidmagic.util.constants.CustomTranslations;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class InitialGuideBookScreen extends Screen {

    private final ResourceLocation TEXTURE;
    private final ItemStack itemInHand;
    private final List<Item> entries;

    public InitialGuideBookScreen(ItemStack itemInHand) {
        super(new TranslationTextComponent(CustomTranslations.GUIDE_BOOK_SCREEN.get()));
        this.itemInHand = itemInHand;
        this.TEXTURE = IdBuilder.mc("textures/block/dirt.png");
        this.entries =
                ModRegistries.ITEMS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        for (int i = 0; i < entries.size(); i++) {
            Item current = entries.get(i);
            VoidMagicClient.getGame().getItemRenderer().renderItemIntoGUI(current.getDefaultInstance(), 16 * i, 16);
        }
    }

}
