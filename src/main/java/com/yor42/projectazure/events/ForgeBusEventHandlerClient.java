package com.yor42.projectazure.events;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.network.packets.EntityInteractionPacket;
import com.yor42.projectazure.network.packets.GunFiredPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeBusEventHandlerClient {

    private static boolean keyFirePressedMainhand = false;
    private static boolean keyFirePressedOffhand = false;

    /*
    Snippets from Techguns 2.
    thank you, pWn3d1337!
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseEvent(InputEvent.RawMouseEvent event){
        LocalPlayer player = Minecraft.getInstance().player;
        //check if game has focus
        if(Minecraft.getInstance().isWindowActive() && player != null && !Minecraft.getInstance().isPaused() && Minecraft.getInstance().screen == null){
            if(event.getButton() == GLFW_MOUSE_BUTTON_LEFT && player.getMainHandItem().getItem() instanceof ItemGunBase){
                keyFirePressedMainhand = event.getAction() == GLFW_PRESS;
                event.setCanceled(true);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void OnplayerRightClicked(PlayerInteractEvent.RightClickEmpty event){
        Player player = event.getPlayer();
        List<Entity> passengers = player.getPassengers();
        if(!passengers.isEmpty() && player.isShiftKeyDown()){
            for(Entity entity:passengers){
                if(entity instanceof AbstractEntityCompanion){
                    Main.NETWORK.sendToServer(new EntityInteractionPacket(entity.getId(), EntityInteractionPacket.EntityBehaviorType.STOP_RIDING, true));
                }
            }
        }
    }

    @SubscribeEvent
    public static void TickPlayer(TickEvent.PlayerTickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            if (event.player.level.isClientSide()) {

                if (Minecraft.getInstance().isWindowActive() && !event.player.isSpectator()) {
                    ItemStack MainStack = event.player.getMainHandItem();
                    ItemStack OffStack = event.player.getOffhandItem();
                    if (!MainStack.isEmpty() && MainStack.getItem() instanceof ItemGunBase && ((ItemGunBase) MainStack.getItem()).ShouldFireWithLeftClick()) {
                        if (keyFirePressedMainhand) {
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
                                keyFirePressedMainhand = false;
                            }

                        }
                    } else {
                        keyFirePressedMainhand = false;
                    }


                } else {
                    keyFirePressedMainhand = false;
                    keyFirePressedOffhand = false;
                }

            }
        }
    }

    @SubscribeEvent
    public static void RenderEntityEvent(RenderLivingEvent.Pre event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();

            ItemStack mainStack = player.getMainHandItem();

            if(!mainStack.isEmpty() && mainStack.getItem() instanceof ItemGunBase && ((ItemGunBase) mainStack.getItem()).ShouldDoBowPose()){
                EntityModel<?> model = event.getRenderer().getModel();
                if(model instanceof PlayerModel){
                    PlayerModel<?> playermodel = (PlayerModel<?>) model;

                    if(player.getMainArm() == HumanoidArm.RIGHT){
                        playermodel.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
                    }
                    else{
                        playermodel.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
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
