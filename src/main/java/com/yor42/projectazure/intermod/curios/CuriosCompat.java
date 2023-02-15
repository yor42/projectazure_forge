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
import com.yor42.projectazure.gameobject.capability.CuriosEnergyCapabilityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.yor42.projectazure.libs.utils.CompatibilityUtils.CURIOS_MODID;

public class CuriosCompat {

    public static void sendImc(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
        InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
    }

    public static ICapabilityProvider addCapability(ItemStack stack){
        return new CuriosCapabilityProvider(stack);
    }

    public static ICapabilityProvider addEnergyCapability(ItemStack stack, int energysize){
        return new CuriosEnergyCapabilityProvider(stack, energysize);
    }

    public static ItemStack getCurioItemStack(PlayerEntity entity, Predicate<ItemStack> predicate){
        AtomicReference<ItemStack> returnstack = new AtomicReference<>(ItemStack.EMPTY);
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                ICurioStacksHandler stacksHandler = entry.getValue();
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if(predicate.test(stack)){
                        returnstack.set(stack);
                    }
                }
            }
        });
        return returnstack.get();
    }

}
