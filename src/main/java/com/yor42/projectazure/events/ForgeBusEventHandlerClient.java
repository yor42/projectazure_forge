package com.yor42.projectazure.events;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.network.packets.EntityInteractionPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData.TeamListCLIENT;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeBusEventHandlerClient {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void OnplayerRightClicked(PlayerInteractEvent.RightClickEmpty event){
        PlayerEntity player = event.getPlayer();
        List<Entity> passengers = player.getPassengers();
        if(!passengers.isEmpty() && player.isShiftKeyDown()){
            for(Entity entity:passengers){
                if(entity instanceof AbstractEntityCompanion){
                    Main.NETWORK.sendToServer(new EntityInteractionPacket(entity.getId(), EntityInteractionPacket.EntityBehaviorType.STOP_RIDING, true));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onGameOverlayRender(RenderGameOverlayEvent.Pre event){

        if(CompatibilityUtils.isCurioLoaded() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET){
            CuriosCompat.RenderCurioGameOverlay(event);
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event){
        Main.CRUSHING_REGISTRY.clearRecipes();
        TeamListCLIENT.clear();
    }

}
