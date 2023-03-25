package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
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

    public static ChangeContainerPacket decode (final FriendlyByteBuf buffer){
        final ItemStack riggingstack = buffer.readItem();
        final UUID id = buffer.readUUID();
        return new ChangeContainerPacket(riggingstack, id);
    }

    public static void encode(final ChangeContainerPacket msg, final FriendlyByteBuf buffer){
        buffer.writeItem(msg.riggingstack);
        buffer.writeUUID(msg.EntityID);
    }

    public static void handle(final ChangeContainerPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();
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
