package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.network.serverEvents;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class selectedStarterPacket {
    private final int starterID;

    public selectedStarterPacket(int id) {
        this.starterID = id;
    }

    public static selectedStarterPacket decode (final FriendlyByteBuf  buffer){
        final int starterid = buffer.readInt();
        return new selectedStarterPacket(starterid);
    }

    public static void encode(final selectedStarterPacket msg, final FriendlyByteBuf  buffer){
        buffer.writeInt(msg.starterID);
    }

    public static void handle(final selectedStarterPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                serverEvents.spawnStarter(playerEntity, msg.starterID);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
