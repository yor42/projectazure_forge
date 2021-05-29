package com.yor42.projectazure.events;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.network.packets.GunFiredPacket;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
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
