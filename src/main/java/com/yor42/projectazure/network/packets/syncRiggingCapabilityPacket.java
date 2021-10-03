package com.yor42.projectazure.network.packets;

public class syncRiggingCapabilityPacket {
/*
    private final CompoundNBT InventoryCompound, FuelTankCompound;
    private final ItemStack itemStack;


    public syncRiggingCapabilityPacket(RiggingItemCapabilityProvider capability) {
        this.InventoryCompound = capability.getEquipments().serializeNBT();
        this.FuelTankCompound = null;//capability.writeToNBT(new CompoundNBT());
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

 */


}
