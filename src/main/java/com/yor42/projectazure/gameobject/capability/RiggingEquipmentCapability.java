package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.IRiggingContainerSupplier;
import com.yor42.projectazure.network.packets.syncRiggingInventoryPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

public abstract class RiggingEquipmentCapability implements INamedContainerProvider, IRiggingContainerSupplier {

    protected final LivingEntity entity;
    protected final ItemStack stack;

    protected final LazyOptional<ItemStackHandler> riggingItemCapability = LazyOptional.of(() -> this.equipments);

    protected final ItemStackHandler equipments = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            RiggingEquipmentCapability.this.saveAll();

        }
    };

    public RiggingEquipmentCapability(ItemStack stack){
        this(stack, null, false);
    }

    public RiggingEquipmentCapability(ItemStack stack, LivingEntity entity){
        this(stack, entity, true);
    }

    public RiggingEquipmentCapability(ItemStack stack, LivingEntity entity, boolean isEquippedonShip){
        this.stack = stack;
        this.entity = entity;
        this.loadEquipments(this.getNBT(stack));
        this.equipments.setSize(this.getSlotCounts());
    }

    public int getSlotCounts(){
        return this.getGunSlotCount()+getAAslotCount()+getTorpedoSlotCount();
    };

    public abstract int getAAslotCount();
    public abstract int getGunSlotCount();
    public abstract int getTorpedoSlotCount();

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

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.rigginginventory");
    }
}
