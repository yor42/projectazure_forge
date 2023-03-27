package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CreateTeamPacket {

    private final UUID plauerUUID;

    public CreateTeamPacket(UUID playerUUID) {
        this.plauerUUID = playerUUID;
    }

    public static CreateTeamPacket decode (final FriendlyByteBuf buffer){
        final UUID playerUUID = buffer.readUUID();
        return new CreateTeamPacket(playerUUID);
    }

    public static void encode(final CreateTeamPacket msg, final FriendlyByteBuf buffer){
        buffer.writeUUID(msg.plauerUUID);
    }

    public static void handle(final CreateTeamPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();
            final ServerLevel world = playerEntity.getLevel();
            if(playerEntity.getUUID().equals(msg.plauerUUID)) {
                ProjectAzureWorldSavedData data = ProjectAzureWorldSavedData.getSaveddata(world);
                data.createteam(playerEntity);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
