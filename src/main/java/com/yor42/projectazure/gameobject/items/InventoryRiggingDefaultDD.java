package com.yor42.projectazure.gameobject.items;

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

public class InventoryRiggingDefaultDD implements INamedContainerProvider, IRiggingContainerSupplier {

    private final LivingEntity entity;
    private final ItemStack stack;

    private final LazyOptional<ItemStackHandler> riggingItemCap = LazyOptional.of(() -> this.equipments);

    private final ItemStackHandler equipments = new ItemStackHandler(6){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            InventoryRiggingDefaultDD.this.saveAll();

        }
    };

    public InventoryRiggingDefaultDD(ItemStack stack, LivingEntity entity){
        this.stack = stack;
        this.entity = entity;
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

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack)
    {
        if(!serverPlayerEntity.world.isRemote)
        {
            NetworkHooks.openGui(serverPlayerEntity, new InventoryRiggingDefaultDD(stack, serverPlayerEntity));//packetBuffer.writeItemStack(stack, false).writeByte(screenID));
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
