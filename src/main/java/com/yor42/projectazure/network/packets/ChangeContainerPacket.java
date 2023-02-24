package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class ChangeContainerPacket {
    private final ItemStack riggingstack;
    private final UUID EntityID;

    public ChangeContainerPacket(ItemStack riggingstack, UUID EntityID) {
        this.riggingstack = riggingstack;
        this.EntityID = EntityID;
    }

    public static ChangeContainerPacket decode (final PacketBuffer buffer){
        final ItemStack riggingstack = buffer.readItem();
        final UUID id = buffer.readUUID();
        return new ChangeContainerPacket(riggingstack, id);
    }

    public static void encode(final ChangeContainerPacket msg, final PacketBuffer buffer){
        buffer.writeItem(msg.riggingstack);
        buffer.writeUUID(msg.EntityID);
    }

    public static void handle(final ChangeContainerPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                NetworkHooks.openGui(playerEntity, new RiggingContainer.Provider(msg.riggingstack, msg.EntityID), buf -> {
                    buf.writeItem(msg.riggingstack);
                    buf.writeUUID(msg.EntityID);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
