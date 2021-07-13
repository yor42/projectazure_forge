package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.network.serverEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class ChangeEntityBehaviorPacket {

    private final int EntityID;
    private final EntityBehaviorType behaviorType;
    private final boolean value;

    public ChangeEntityBehaviorPacket(int EntityID, EntityBehaviorType behaviorType, boolean value) {
        this.EntityID = EntityID;
        this.behaviorType = behaviorType;
        this.value = value;
    }

    public static ChangeEntityBehaviorPacket decode (final PacketBuffer buffer){
        final int ID = buffer.readInt();
        final EntityBehaviorType BehaviorType = buffer.readEnumValue(EntityBehaviorType.class);
        final boolean value = buffer.readBoolean();
        return new ChangeEntityBehaviorPacket(ID, BehaviorType, value);
    }

    public static void encode(final ChangeEntityBehaviorPacket msg, final PacketBuffer buffer){
        buffer.writeInt(msg.EntityID);
        buffer.writeEnumValue(msg.behaviorType);
        buffer.writeBoolean(msg.value);
    }

    public static void handle(final ChangeEntityBehaviorPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                final ServerWorld world = Objects.requireNonNull(ctx.get().getSender()).getServerWorld();
                Entity entity = world.getEntityByID(msg.EntityID);
                if(entity instanceof AbstractEntityCompanion){
                    switch(msg.behaviorType){
                        case SIT:
                            ((AbstractEntityCompanion) entity).func_233687_w_(msg.value);
                            break;
                        case HOMEMODE:
                            ((AbstractEntityCompanion) entity).setFreeRoaming(msg.value);
                            break;
                        case ITEMPICKUP:
                            ((AbstractEntityCompanion) entity).setPickupItem(msg.value);
                            break;
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum EntityBehaviorType {
        SIT,
        HOMEMODE,
        ITEMPICKUP;
    }
}
