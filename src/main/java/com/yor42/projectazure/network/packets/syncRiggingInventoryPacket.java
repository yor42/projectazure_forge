package com.yor42.projectazure.network.packets;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class syncRiggingInventoryPacket {

    private final CompoundNBT compound;
    private final ItemStack itemStack;


    public syncRiggingInventoryPacket(CompoundNBT compound, ItemStack stack) {
        this.compound = compound;
        this.itemStack = stack;
    }

    public static syncRiggingInventoryPacket decode(final PacketBuffer buffer)
    {
        final CompoundNBT compound = buffer.readCompoundTag();
        final ItemStack stack = buffer.readItemStack();

        return new syncRiggingInventoryPacket(compound, stack);
    }

    public static void encode(final syncRiggingInventoryPacket message, final PacketBuffer buffer){
        buffer.writeCompoundTag(message.compound);
        buffer.writeItemStack(message.itemStack);
    }

    public static void handle(final syncRiggingInventoryPacket message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {

            message.itemStack.getOrCreateTag().put("Inventory", message.compound);
        }));

        ctx.get().setPacketHandled(true);
    }


}
