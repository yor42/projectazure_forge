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

public class RiggingDefaultDDEquipmentCapability implements INamedContainerProvider, IRiggingContainerSupplier {

    private final LivingEntity entity;
    private final ItemStack stack;
    private boolean isEquippedonShip;

    private final LazyOptional<ItemStackHandler> riggingItemCapability = LazyOptional.of(() -> this.equipments);

    private final ItemStackHandler equipments = new ItemStackHandler(6){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            RiggingDefaultDDEquipmentCapability.this.saveAll();

        }
    };

    public RiggingDefaultDDEquipmentCapability(ItemStack stack){
        this.entity = null;
        this.stack = stack;
        this.loadEquipments(this.getNBT(stack));
    }

    public RiggingDefaultDDEquipmentCapability(ItemStack stack, LivingEntity entity){
        this(stack, entity, true);
    }

    public RiggingDefaultDDEquipmentCapability(ItemStack stack, LivingEntity entity, boolean isEquippedonShip){
        this.stack = stack;
        this.entity = entity;
        this.isEquippedonShip = isEquippedonShip;
        this.loadEquipments(this.getNBT(stack));
    }

    public void saveAll(){
        this.saveEquipments(this.getNBT(this.stack));
        this.sendpacket();
    }

    private void sendpacket() {
        if(this.entity instanceof PlayerEntity){
            Main.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new syncRiggingInventoryPacket(this.getEquipments().serializeNBT(), this.stack));
        }
    }

    public void saveEquipments(CompoundNBT nbt) {
        nbt.put("Inventory" ,this.equipments.serializeNBT());
    }

    public CompoundNBT getNBT(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    public void loadEquipments(CompoundNBT nbt){
        this.equipments.deserializeNBT(nbt.getCompound("Inventory"));
    }

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack){
        openGUI(serverPlayerEntity, stack, false);
    }

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack, boolean isEquippedonShip)
    {
        if(!serverPlayerEntity.world.isRemote)
        {
            NetworkHooks.openGui(serverPlayerEntity, new RiggingDefaultDDEquipmentCapability(stack, serverPlayerEntity, isEquippedonShip));//packetBuffer.writeItemStack(stack, false).writeByte(screenID));
        }
    }

    public ItemStackHandler getEquipments() {
        return this.equipments;
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new RiggingContainerDDDefault(windowID, playerInventory, this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.rigginginventory");
    }
}
