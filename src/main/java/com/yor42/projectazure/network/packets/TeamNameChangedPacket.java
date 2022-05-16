package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class TeamNameChangedPacket {
    private final UUID teamUUID;
    private final String name;
    public TeamNameChangedPacket(UUID teamUUID, String name) {
        this.teamUUID = teamUUID;
        this.name = name;
    }

    public static TeamNameChangedPacket decode (final PacketBuffer buffer){
        final UUID teamUUID = buffer.readUUID();
        final String name = buffer.readUtf();
        return new TeamNameChangedPacket(teamUUID, name);
    }

    public static void encode(final TeamNameChangedPacket msg, final PacketBuffer buffer){
        buffer.writeUUID(msg.teamUUID);
        buffer.writeUtf(msg.name);
    }

    public static void handle(final TeamNameChangedPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                ServerWorld world = playerEntity.getLevel();
                ProjectAzureWorldSavedData.getSaveddata(world).ChangeTeamName(msg.teamUUID, msg.name);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
