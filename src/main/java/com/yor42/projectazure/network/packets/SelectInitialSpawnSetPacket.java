package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.network.serverEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SelectInitialSpawnSetPacket {
    private final byte id;
    private final int handindex;

    public SelectInitialSpawnSetPacket(byte id, int index){
        this.id = id;
        this.handindex = index;
    }

    public SelectInitialSpawnSetPacket(byte id, InteractionHand hand){
        this(id, hand.ordinal());
    }

    public static SelectInitialSpawnSetPacket decode (final FriendlyByteBuf buffer){
        final byte id = buffer.readByte();
        final int handindex = buffer.readInt();
        return new SelectInitialSpawnSetPacket(id, handindex);
    }

    public static void encode(final SelectInitialSpawnSetPacket msg, final FriendlyByteBuf buffer){
        buffer.writeByte(msg.id);
        buffer.writeInt(msg.handindex);
    }

    public static void handle(final SelectInitialSpawnSetPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();

            if (playerEntity != null) {
                serverEvents.selectStartkit(playerEntity, msg.id, InteractionHand.values()[msg.handindex]);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
