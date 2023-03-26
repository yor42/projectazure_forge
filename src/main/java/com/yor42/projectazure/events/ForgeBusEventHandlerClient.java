package com.yor42.projectazure.events;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData.TeamListCLIENT;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeBusEventHandlerClient {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onGameOverlayRender(RenderGameOverlayEvent.PreLayer event){

        if(CompatibilityUtils.isCurioLoaded() && event.getOverlay() == ForgeIngameGui.HELMET_ELEMENT){
            CuriosCompat.RenderCurioGameOverlay(event);
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event){
        Main.CRUSHING_REGISTRY.clearRecipes();
        TeamListCLIENT.clear();
    }

}
