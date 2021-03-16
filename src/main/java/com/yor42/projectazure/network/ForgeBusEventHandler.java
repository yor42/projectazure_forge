package com.yor42.projectazure.network;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.network.packets.GunFiredPacket;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
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

    @SubscribeEvent
    public static void TickPlayer(TickEvent.PlayerTickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            if (event.player.world.isRemote()) {

                ClientProxy client = ClientProxy.getClientProxy();
                if (Minecraft.getInstance().isGameFocused() && !event.player.isSpectator()) {
                    ItemStack MainStack = event.player.getHeldItemMainhand();
                    ItemStack OffStack = event.player.getHeldItemOffhand();
                    if (!MainStack.isEmpty() && MainStack.getItem() instanceof ItemGunBase && ((ItemGunBase) MainStack.getItem()).ShouldFireWithLeftClick()) {
                        if (client.keyFirePressedMainhand) {
                            ItemGunBase gun = ((ItemGunBase) MainStack.getItem());
                            Main.NETWORK.sendToServer(new GunFiredPacket(false, false));

                            if (gun.isSemiAuto()) {
                                client.keyFirePressedMainhand = false;
                            }

                        }
                    } else {
                        client.keyFirePressedMainhand = false;
                    }


                } else {
                    client.keyFirePressedMainhand = false;
                    client.keyFirePressedOffhand = false;
                }

            }
        }
    }

    @SubscribeEvent
    public static void RenderEntityEvent(RenderLivingEvent.Pre event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();

            ItemStack mainStack = player.getHeldItemMainhand();

            if(!mainStack.isEmpty() && mainStack.getItem() instanceof ItemGunBase && ((ItemGunBase) mainStack.getItem()).ShouldDoBowPose()){
                EntityModel<?> model = event.getRenderer().getEntityModel();
                if(model instanceof PlayerModel){
                    PlayerModel<?> playermodel = (PlayerModel<?>) model;

                    if(player.getPrimaryHand() == HandSide.RIGHT){
                        playermodel.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                    }
                    else{
                        playermodel.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                    }
                }
            }
        }
    }
}
