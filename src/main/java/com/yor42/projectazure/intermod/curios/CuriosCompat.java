package com.yor42.projectazure.intermod.curios;
/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

import com.yor42.projectazure.gameobject.capability.CuriosCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import static com.yor42.projectazure.libs.Constants.CURIOS_MODID;

public class CuriosCompat {

    public static void sendImc(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
        InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
    }

    public static ICapabilityProvider addCapability(ItemStack stack){
        return new CuriosCapabilityProvider(stack);
    }

}
