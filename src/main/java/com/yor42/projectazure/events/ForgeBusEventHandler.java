package com.yor42.projectazure.events;

import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEventHandler {


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if(event.phase == TickEvent.Phase.START){

            ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability(event.player);

            if(cap.getOffHandFireDelay()>0){
                cap.setOffHandFireDelay(cap.getOffHandFireDelay()-1);
            }
            if(cap.getMainHandFireDelay()>0){
                cap.setMainHandFireDelay(cap.getMainHandFireDelay()-1);
            }
        }
    }
}
