package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ReopenEntityInventoryPacket {

    private final UUID entityUUID;
    public ReopenEntityInventoryPacket(UUID entityUUID){
        this.entityUUID = entityUUID;
    };

    public static ReopenEntityInventoryPacket decode (final PacketBuffer buffer){
        final UUID id = buffer.readUUID();
        return new ReopenEntityInventoryPacket(id);
    }

    public static void encode(final ReopenEntityInventoryPacket msg, final PacketBuffer buffer){
        buffer.writeUUID(msg.entityUUID);
    }
    public static void handle(final ReopenEntityInventoryPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                final World world = playerEntity.getCommandSenderWorld();
                Entity entity = ((ServerWorld) world).getEntity(msg.entityUUID);


                if (entity instanceof AbstractEntityCompanion) {
                    ((AbstractEntityCompanion) entity).openGUI(playerEntity);
                    ProjectAzurePlayerCapability.getCapability(playerEntity).lastGUIOpenedEntityID = null;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
