package com.yor42.projectazure.events;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid= Constants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandlerClient {
    @SubscribeEvent
    public static void registerItemColorHandlers(@Nonnull ColorHandlerEvent.Item event) {
        ItemColors color = event.getItemColors();
        color.register((stack, index)->{
            if(index == 0){
                return -1;
            }
            else{
                if(PotionUtils.getPotion(stack)== Potions.EMPTY){
                    return -1;
                }
                return PotionUtils.getColor(stack);
            }
        }, RegisterItems.SYRINGE.get());
    }
}
