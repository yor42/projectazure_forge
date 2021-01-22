package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.network.serverAction;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class selectedStarterPacket {
    private final int starterID;

    public selectedStarterPacket(int valid) {
        this.starterID = valid;
    }

    public static selectedStarterPacket decode (final PacketBuffer buffer){
        final int starterid = buffer.readInt();
        return new selectedStarterPacket(starterid);
    }

    public static void encode(final selectedStarterPacket msg, final PacketBuffer buffer){
        buffer.writeInt(msg.starterID);
    }

    public static void handle(final selectedStarterPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                serverAction.spawnStarter(playerEntity, msg.starterID);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
