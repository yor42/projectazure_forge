package com.yor42.projectazure.events;

import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusEventHandlerClient {

    /*
    Snippets from Techguns 2.
    thank you, pWn3d1337!
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseEvent(InputEvent.RawMouseEvent event){
        ClientPlayerEntity player = Minecraft.getInstance().player;
        //check if game has focus
        if(Minecraft.getInstance().isGameFocused() && player != null && !Minecraft.getInstance().isGamePaused() && Minecraft.getInstance().currentScreen == null){
            if(event.getButton() == GLFW_MOUSE_BUTTON_LEFT && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemGunBase){

                ClientProxy client = ClientProxy.getClientProxy();
                client.keyFirePressedMainhand = event.getAction() == GLFW_PRESS;

                event.setCanceled(true);
            }
        }
    }

}
