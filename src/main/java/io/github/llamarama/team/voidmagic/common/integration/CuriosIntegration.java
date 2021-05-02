package io.github.llamarama.team.voidmagic.common.integration;

import io.github.llamarama.team.voidmagic.common.util.constants.ModConstants;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.SlotTypeMessage;

public class CuriosIntegration implements IIntegrator {

    private static CuriosIntegration instance;

    public static CuriosIntegration getInstance() {
        if (instance == null) {
            instance = new CuriosIntegration();
        }

        return instance;
    }

    @Override
    public void enableSupport(final InterModEnqueueEvent event) {
        InterModComms.sendTo(ModConstants.CURIOS_ID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("charm").build());
        InterModComms.sendTo(ModConstants.CURIOS_ID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("ring").size(2).build());
        InterModComms.sendTo(ModConstants.CURIOS_ID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("necklace").build());
        InterModComms.sendTo(ModConstants.CURIOS_ID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("belt").build());
    }

}
