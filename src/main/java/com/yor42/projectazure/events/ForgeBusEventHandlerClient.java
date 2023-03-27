package com.yor42.projectazure.events;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.interfaces.IHelmetOverlay;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.ClientUtils;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null){
            return;
        }

        if(event.getOverlay() == ForgeIngameGui.HELMET_ELEMENT) {
            if(!Minecraft.getInstance().options.getCameraType().isFirstPerson()){
                return;
            }
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            Item helmetItem = helmet.getItem();
            if(helmetItem instanceof IHelmetOverlay){
                ClientUtils.renderTextureOverlay(((IHelmetOverlay) helmetItem).getOverlayTexture(), 1.0f);
            }
            else {
                if (CompatibilityUtils.isCurioLoaded()) {
                    CuriosCompat.RenderCurioHelmetOverlay(player);
                }
            }
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event){
        Main.CRUSHING_REGISTRY.clearRecipes();
        TeamListCLIENT.clear();
    }

}
