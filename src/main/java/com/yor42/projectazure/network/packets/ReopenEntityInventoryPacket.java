package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.containers.entity.CompanionContainerProvider;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class ReopenEntityInventoryPacket {

    private final UUID entityUUID;
    public ReopenEntityInventoryPacket(UUID entityUUID){
        this.entityUUID = entityUUID;
    }

    public static ReopenEntityInventoryPacket decode (final FriendlyByteBuf buffer){
        final UUID id = buffer.readUUID();
        return new ReopenEntityInventoryPacket(id);
    }

    public static void encode(final ReopenEntityInventoryPacket msg, final FriendlyByteBuf buffer){
        buffer.writeUUID(msg.entityUUID);
    }
    public static void handle(final ReopenEntityInventoryPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                final Level world = playerEntity.getCommandSenderWorld();
                Entity entity = ((ServerLevel) world).getEntity(msg.entityUUID);


                if (entity instanceof AbstractEntityCompanion) {
                    NetworkHooks.openGui(playerEntity, new CompanionContainerProvider((AbstractEntityCompanion) entity), buf -> buf.writeInt(entity.getId()));
                    ProjectAzurePlayerCapability.getCapability(playerEntity).lastGUIOpenedEntityID = null;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
