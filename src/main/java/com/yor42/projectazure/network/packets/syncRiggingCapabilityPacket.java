package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class syncRiggingCapabilityPacket {

    private final CompoundNBT InventoryCompound, FuelTankCompound;
    private final ItemStack itemStack;


    public syncRiggingCapabilityPacket(RiggingInventoryCapability capability) {
        this.InventoryCompound = capability.getEquipments().serializeNBT();
        this.FuelTankCompound = capability.getFuelTank().writeToNBT(new CompoundNBT());
        this.itemStack = capability.getItemStack();
    }

    public syncRiggingCapabilityPacket(CompoundNBT InventoryCompound, CompoundNBT FuelTankCompound, ItemStack stack){
        this.InventoryCompound = InventoryCompound;
        this.FuelTankCompound = FuelTankCompound;
        this.itemStack = stack;
    }



    public static syncRiggingCapabilityPacket decode(final PacketBuffer buffer)
    {
        final CompoundNBT compound1 = buffer.readCompoundTag();
        final CompoundNBT compound2 = buffer.readCompoundTag();
        final ItemStack stack = buffer.readItemStack();

        return new syncRiggingCapabilityPacket(compound1, compound2, stack);
    }

    public static void encode(final syncRiggingCapabilityPacket message, final PacketBuffer buffer){
        buffer.writeCompoundTag(message.InventoryCompound);
        buffer.writeCompoundTag(message.FuelTankCompound);
        buffer.writeItemStack(message.itemStack);
    }

    public static void handle(final syncRiggingCapabilityPacket message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {

            message.itemStack.getOrCreateTag().put("Inventory", message.InventoryCompound);
            message.itemStack.getOrCreateTag().put("fuels", message.FuelTankCompound);
        }));

        ctx.get().setPacketHandled(true);
    }


}
