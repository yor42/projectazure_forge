package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.IRiggingContainerSupplier;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainerDDDefault;
import com.yor42.projectazure.network.packets.syncRiggingInventoryPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class RiggingDefaultDDEquipmentCapability extends RiggingEquipmentCapability {

    public RiggingDefaultDDEquipmentCapability(ItemStack stack) {
        super(stack);
    }

    public RiggingDefaultDDEquipmentCapability(ItemStack stack, LivingEntity entity) {
        super(stack, entity);
    }

    public RiggingDefaultDDEquipmentCapability(ItemStack stack, LivingEntity entity, boolean isEquippedonShip) {
        super(stack, entity, isEquippedonShip);
    }

    @Override
    public int getAAslotCount() {
        return 1;
    }

    @Override
    public int getGunSlotCount() {
        return 2;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 3;
    }

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack) {
        openGUI(serverPlayerEntity, stack, false);
    }

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack, boolean isEquippedonShip) {
        if (!serverPlayerEntity.world.isRemote) {
            NetworkHooks.openGui(serverPlayerEntity, new RiggingDefaultDDEquipmentCapability(stack, serverPlayerEntity, isEquippedonShip));//packetBuffer.writeItemStack(stack, false).writeByte(screenID));
        }
    }

    public ItemStackHandler getEquipments() {
        return this.equipments;
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new RiggingContainerDDDefault(windowID, playerInventory, this);
    }
}
