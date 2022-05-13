package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RemoveTeamPacket {

    private final UUID teamUUID;

    public RemoveTeamPacket(UUID teamUUID){
        this.teamUUID = teamUUID;
    }

    public static RemoveTeamPacket decode (final PacketBuffer buffer){
        final UUID id = buffer.readUUID();
        return new RemoveTeamPacket(id);
    }

    public static void encode(final RemoveTeamPacket msg, final PacketBuffer buffer){
        buffer.writeUUID(msg.teamUUID);
    }

    public static void handle(final RemoveTeamPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            final ServerWorld world = playerEntity.getLevel();
            ProjectAzureWorldSavedData data = ProjectAzureWorldSavedData.getSaveddata(world);
            data.removeTeambyUUID(msg.teamUUID);
        });
        ctx.get().setPacketHandled(true);
    }
}
