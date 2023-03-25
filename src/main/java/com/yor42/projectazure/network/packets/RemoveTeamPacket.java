package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RemoveTeamPacket {

    private final UUID teamUUID;

    public RemoveTeamPacket(UUID teamUUID){
        this.teamUUID = teamUUID;
    }

    public static RemoveTeamPacket decode (final FriendlyByteBuf buffer){
        final UUID id = buffer.readUUID();
        return new RemoveTeamPacket(id);
    }

    public static void encode(final RemoveTeamPacket msg, final FriendlyByteBuf buffer){
        buffer.writeUUID(msg.teamUUID);
    }

    public static void handle(final RemoveTeamPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();
            final ServerLevel world = playerEntity.getLevel();
            ProjectAzureWorldSavedData data = ProjectAzureWorldSavedData.getSaveddata(world);
            data.removeTeambyUUID(msg.teamUUID);
        });
        ctx.get().setPacketHandled(true);
    }
}
