package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Supplier;

public class syncEntityInventoryPacket {

    private final CompoundNBT compound;
    private final int entityID;


    public syncEntityInventoryPacket(CompoundNBT compound, int entityID) {
        this.compound = compound;
        this.entityID = entityID;
    }

    public static syncEntityInventoryPacket decode(final PacketBuffer buffer)
    {
        final CompoundNBT compound = buffer.readCompoundTag();
        final int entityID = buffer.readInt();

        return new syncEntityInventoryPacket(compound, entityID);
    }

    public static void encode(final syncEntityInventoryPacket message, final PacketBuffer buffer){
        buffer.writeCompoundTag(message.compound);
        buffer.writeInt(message.entityID);
    }

    public static void handle(final syncEntityInventoryPacket message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {

            final EntityKansenBase Entity = (EntityKansenBase) Minecraft.getInstance().player.world.getEntityByID(message.entityID);
            ItemStackHandler handler = Entity.getShipStorage();
            if(handler!=null){
                handler.deserializeNBT(message.compound);
            }
        }));

        ctx.get().setPacketHandled(true);
    }


}
