package com.yor42.projectazure.network;

import com.google.common.base.Throwables;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.network.proxy.ClientProxy;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability.CapabilityID;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getPlayer().world.isRemote) {
            return;
        }
        CompoundNBT playerData = event.getPlayer().getPersistentData();
        CompoundNBT data;

        if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            data = new CompoundNBT();
        }
        else {
            data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        }

        if (!data.getBoolean("PRJA:gotStarterCube")) {
            if (event.getPlayer().inventory.isEmpty()){
                PlayerEntity player = event.getPlayer();
                UUID yorUUID = UUID.fromString("d45160dc-ae0b-4f7c-b44a-b535a48182d2");
                boolean isDev = player.getUniqueID().equals(yorUUID);
                player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.Rainbow_Wisdom_Cube.get()));
                if(isDev){
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWM_AYANAMI.get()));
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWM_ENTERPRISE.get()));
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWN_GANGWON.get()));
                }
            }
            data.putBoolean(defined.MODID+":gotStarterCube", true);
            playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
        }

    }

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



    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            try {
                event.addCapability(CapabilityID, ProjectAzurePlayerCapability.createNewCapability((PlayerEntity) event.getObject()));

            }
            catch (Exception e) {
                Main.LOGGER.error("Failed to attach capabilities to player. Player: {}", event.getObject());
                Throwables.propagate(e);
            }
        }
    }

}
