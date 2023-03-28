package com.yor42.projectazure.events;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

    @SubscribeEvent
    public void PlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        ProjectAzureWorldSavedData.TeamListCLIENT.clear();
    }
}
