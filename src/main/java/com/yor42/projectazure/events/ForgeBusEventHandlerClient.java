package com.yor42.projectazure.events;

import com.tac.guns.util.CurioCompatUtil;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.items.ItemNightVisionHelmet;
import com.yor42.projectazure.interfaces.IHelmetOverlay;
import com.yor42.projectazure.interfaces.IShaderEquipment;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.ClientUtils;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.mixin.GameRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.misc.ProjectAzureWorldSavedData.TeamListCLIENT;

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

    @Nullable
    public static ResourceLocation activeShader = null;
    private static ResourceLocation cachedshader = null;
    private static boolean isActive = false;
    private static boolean isActiveOld = false;
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderShader(TickEvent.RenderTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        GameRenderer renderer = minecraft.gameRenderer;
        LocalPlayer player = minecraft.player;

        if (player == null || player.isSpectator()) {
            return;
        }
        ResourceLocation location = null;
        ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack shaderStack = ItemStack.EMPTY;
        if (headStack.getItem() instanceof IShaderEquipment) {
            shaderStack = headStack;
        } else if (CompatibilityUtils.isCurioLoaded()) {
            shaderStack = CuriosCompat.FindCurioShader(player);
        }

        if (shaderStack.getItem() instanceof IShaderEquipment shaderEquipment) {
            location = shaderEquipment.shaderLocation(shaderStack);
            isActive = shaderEquipment.shouldDisplayShader(shaderStack) && minecraft.options.getCameraType().isFirstPerson();
        }

        if(location == null){
            if(cachedshader != null){
                restoreshader(renderer);
            }
            return;
        }

        if(isActive != isActiveOld){
            if(isActive){
                renderer.loadEffect(location);
                cachedshader = activeShader;
            }
            else{
                restoreshader(renderer);
            }
        }
    }

    private static void restoreshader(GameRenderer renderer){
        renderer.shutdownEffect();

        if(cachedshader != null){
            renderer.loadEffect(cachedshader);
            cachedshader = null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.FogColors event) {
        LocalPlayer player = Minecraft.getInstance().player;

        if(player == null){
            return;
        }

        ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack NVG = ItemStack.EMPTY;

        if (headStack.getItem() instanceof ItemNightVisionHelmet){
            NVG = headStack;
        }
        else if(CompatibilityUtils.isCurioLoaded()){
            NVG = CuriosCompat.getCurioItemStack(player, "head", (item)->item.getItem() instanceof ItemNightVisionHelmet);
        }

        if (ItemStackUtils.isOn(NVG)) {
            event.setRed(0);
            event.setGreen(0);
            event.setBlue(0);
        }
    }

}
