package com.yor42.projectazure.events;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.network.packets.GunFiredPacket;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeBusEventHandlerClient {


    /*
    Snippets from Techguns 2.
    thank you, pWn3d1337!
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseEvent(InputEvent.RawMouseEvent event){
        ClientPlayerEntity player = Minecraft.getInstance().player;
        //check if game has focus
        if(Minecraft.getInstance().isWindowActive() && player != null && !Minecraft.getInstance().isPaused() && Minecraft.getInstance().screen == null){
            if(event.getButton() == GLFW_MOUSE_BUTTON_LEFT && player.getMainHandItem().getItem() instanceof ItemGunBase){
                ClientProxy client = ClientProxy.getClientProxy();
                client.keyFirePressedMainhand = event.getAction() == GLFW_PRESS;
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void TickPlayer(TickEvent.PlayerTickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            if (event.player.level.isClientSide()) {

                ClientProxy client = ClientProxy.getClientProxy();
                if (Minecraft.getInstance().isWindowActive() && !event.player.isSpectator()) {
                    ItemStack MainStack = event.player.getMainHandItem();
                    ItemStack OffStack = event.player.getOffhandItem();
                    if (!MainStack.isEmpty() && MainStack.getItem() instanceof ItemGunBase && ((ItemGunBase) MainStack.getItem()).ShouldFireWithLeftClick()) {
                        if (client.keyFirePressedMainhand) {
                            ItemGunBase gun = ((ItemGunBase) MainStack.getItem());
                            ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(event.player);
                            int mainDelay = capability.getMainHandFireDelay();
                            int offDelay = capability.getOffHandFireDelay();
                            boolean hasAmmo = getRemainingAmmo(MainStack)>0;
                            if(mainDelay <= 0) {
                                if(hasAmmo) {
                                    if (MinecraftForge.EVENT_BUS.post(new GunFireEvent.PreFire(event.player, event.player.getMainHandItem())))
                                        return;
                                }
                                Main.NETWORK.sendToServer(new GunFiredPacket(false, false));
                            }

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

            ItemStack mainStack = player.getMainHandItem();

            if(!mainStack.isEmpty() && mainStack.getItem() instanceof ItemGunBase && ((ItemGunBase) mainStack.getItem()).ShouldDoBowPose()){
                EntityModel<?> model = event.getRenderer().getModel();
                if(model instanceof PlayerModel){
                    PlayerModel<?> playermodel = (PlayerModel<?>) model;

                    if(player.getMainArm() == HandSide.RIGHT){
                        playermodel.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                    }
                    else{
                        playermodel.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event){
        Main.CRUSHING_REGISTRY.clearRecipes();
    }

}
