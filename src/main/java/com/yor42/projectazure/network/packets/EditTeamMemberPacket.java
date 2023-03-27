package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class EditTeamMemberPacket {

    private final UUID teamUUID, memberUUID;
    private final ACTION action;
    public EditTeamMemberPacket(UUID teamUUID, UUID memberUUID, ACTION action) {
        this.teamUUID = teamUUID;
        this.memberUUID = memberUUID;
        this.action = action;
    }

    public static EditTeamMemberPacket decode (final FriendlyByteBuf buffer){
        final UUID teamUUID = buffer.readUUID();
        final UUID memberUUID = buffer.readUUID();
        final ACTION action = buffer.readEnum(ACTION.class);
        return new EditTeamMemberPacket(teamUUID, memberUUID, action);
    }

    public static void encode(final EditTeamMemberPacket msg, final FriendlyByteBuf buffer){
        buffer.writeUUID(msg.teamUUID);
        buffer.writeUUID(msg.memberUUID);
        buffer.writeEnum(msg.action);
    }

    public static void handle(final EditTeamMemberPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender(
            );
            if (playerEntity != null) {
                ServerLevel world = playerEntity.getLevel();
                ProjectAzureWorldSavedData data = ProjectAzureWorldSavedData.getSaveddata(world);
                Entity entity = world.getEntity(msg.memberUUID);
                switch (msg.action) {
                    case REMOVE:
                        data.removeMember(msg.teamUUID, msg.memberUUID);
                        if(entity instanceof AbstractEntityCompanion){
                            ((AbstractEntityCompanion) entity).removeTeam();
                        }
                        break;
                    case ADD:
                        data.addMember(msg.teamUUID, msg.memberUUID);
                        if(entity instanceof AbstractEntityCompanion) {
                            ((AbstractEntityCompanion) entity).setTeam(msg.teamUUID);
                        }
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum ACTION{
        ADD,
        REMOVE
    }
    
}
