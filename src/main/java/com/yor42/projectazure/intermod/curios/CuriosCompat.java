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
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.yor42.projectazure.libs.utils.CompatibilityUtils.CURIOS_MODID;

public class CuriosCompat {

    public static void sendImc(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
        InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
        InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
    }

    public static ICapabilityProvider addCapability(ItemStack stack){
        return new CuriosCapabilityProvider(stack);
    }

    public static ICapabilityProvider addEnergyCapability(ItemStack stack, int energysize){
        return new CuriosEnergyCapabilityProvider(stack, energysize);
    }

    public static ItemStack getCurioItemStack(LivingEntity entity, Predicate<ItemStack> predicate){
        return getCurioItemStack(entity, null, predicate);
    }

    public static ItemStack getCurioItemStack(LivingEntity entity, @Nullable String identifier, Predicate<ItemStack> predicate){
        AtomicReference<ItemStack> returnstack = new AtomicReference<>(ItemStack.EMPTY);
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();

            if(identifier != null){
                IDynamicStackHandler stackHandler = curios.get(identifier).getStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if(predicate.test(stack)){
                        returnstack.set(stack);
                        return;
                    }
                }
            }

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

    @OnlyIn(Dist.CLIENT)
    public static void RenderCurioGameOverlay(RenderGameOverlayEvent.Pre event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.HELMET){

            if(!Minecraft.getInstance().options.getCameraType().isFirstPerson()){
                return;
            }


            CuriosApi.getCuriosHelper().getCuriosHandler(Minecraft.getInstance().player).ifPresent((cap)->{
                Map<String, ICurioStacksHandler> curios = cap.getCurios();
                IDynamicStackHandler helmetstack = curios.get(SlotTypePreset.HEAD.getIdentifier()).getStacks();
                for (int i = 0; i < helmetstack.getSlots(); i++) {
                    ItemStack stack = helmetstack.getStackInSlot(i);

                    if(stack.isEmpty()){
                        continue;
                    }

                    stack.getItem().renderHelmetOverlay(stack, Minecraft.getInstance().player, event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), event.getPartialTicks());
                }
            });
        }

    }

}
