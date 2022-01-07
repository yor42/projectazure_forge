package com.yor42.projectazure.events;

import com.google.common.base.Throwables;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability.CapabilityID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        if (!event.getPlayer().world.isRemote) {

            CompoundNBT playerData = event.getPlayer().getPersistentData();
            CompoundNBT data;

            if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                data = new CompoundNBT();
            } else {
                data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            }

            boolean flag = !data.getBoolean("PRJA:gotStarterCube");

            if (flag) {
                PlayerEntity player = event.getPlayer();
                UUID yorUUID = UUID.fromString("d45160dc-ae0b-4f7c-b44a-b535a48182d2");
                UUID AoichiID = UUID.fromString("d189319f-ee53-4e80-9472-7c5e4711642e");
                boolean isDev = player.getUniqueID().equals(yorUUID);
                boolean isAoichi = player.getUniqueID().equals(AoichiID);

                ItemStack cubeStack = new ItemStack(registerItems.Rainbow_Wisdom_Cube.get());
                CompoundNBT nbt = cubeStack.getOrCreateTag();
                nbt.putUniqueId("owner", player.getUniqueID());
                cubeStack.setTag(nbt);
                player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), cubeStack);
                if (isDev) {
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWN_NAGATO.get()));
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWM_ENTERPRISE.get()));
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWN_CHEN.get()));
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWN_SHIROKO.get()));
                }
                else if(isAoichi){
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWN_AMIYA.get()));
                    player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), new ItemStack(registerItems.SPAWN_ROSMONTIS.get()));
                }
                data.putBoolean("PRJA:gotStarterCube", true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
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
                Throwables.throwIfUnchecked(e);
            }
        }
    }

}
