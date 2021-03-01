package com.yor42.projectazure.network;

import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class EventHandler {

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
            data.putBoolean("PRJA:gotStarterCube", true);
            playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
        }

    }

}
