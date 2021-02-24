package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.IRiggingContainerSupplier;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainerDDDefault;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
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

//fallback class for new common(dynamic) rigging inventory
public class RiggingInventoryCapability implements INamedContainerProvider, IRiggingContainerSupplier {

    protected final LivingEntity entity;
    protected final ItemStack stack;

    protected final LazyOptional<ItemStackHandler> riggingItemCapability = LazyOptional.of(() -> this.equipments);

    protected final ItemStackHandler equipments = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            RiggingInventoryCapability.this.saveAll();

        }
    };

    public RiggingInventoryCapability(ItemStack stack){
        this(stack, null, false);
    }

    public RiggingInventoryCapability(ItemStack stack, LivingEntity entity){
        this(stack, entity, true);
    }

    public RiggingInventoryCapability(ItemStack stack, LivingEntity entity, boolean isEquippedonShip){
        this.stack = stack;
        this.entity = entity;
        int slotCount = ((ItemRiggingBase) stack.getItem()).getTotalSlotCount();
        this.getEquipments().setSize(slotCount);
        this.loadEquipments(this.getNBT(stack));
    }

    public static void openGUI(ServerPlayerEntity playerIn, ItemStack currentItem) {
        openGUI(playerIn, currentItem, false);
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

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack, boolean isEquippedonShip) {
        if (!serverPlayerEntity.world.isRemote) {
            NetworkHooks.openGui(serverPlayerEntity, new RiggingInventoryCapability(stack, serverPlayerEntity, isEquippedonShip));//packetBuffer.writeItemStack(stack, false).writeByte(screenID));
        }
        Main.PROXY.setSharedStack(stack);
    }

    public void loadEquipments(CompoundNBT nbt){
        this.equipments.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Override
    public ItemStackHandler getEquipments() {
        return this.equipments;
    }

    @Override
    public ItemStack getRigging() {
        return this.stack;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.rigginginventory");
    }
    //null for now.
    //not anymore mofo
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity entity) {
        return new RiggingContainer(windowID, playerInventory, this);
    }
}
