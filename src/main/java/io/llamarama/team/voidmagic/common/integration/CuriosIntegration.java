package io.llamarama.team.voidmagic.common.integration;

import io.llamarama.team.voidmagic.util.constants.StringConstants;
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
        InterModComms.sendTo(StringConstants.CURIOS_ID.get(), SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("charm").build());
        InterModComms.sendTo(StringConstants.CURIOS_ID.get(), SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("ring_1").build());
        InterModComms.sendTo(StringConstants.CURIOS_ID.get(), SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("ring_2").build());
        InterModComms.sendTo(StringConstants.CURIOS_ID.get(), SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("necklace").build());
        InterModComms.sendTo(StringConstants.CURIOS_ID.get(), SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("belt").build());
    }

}
