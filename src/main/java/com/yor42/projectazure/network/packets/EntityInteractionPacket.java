package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.BlockUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class EntityInteractionPacket {

    private final int EntityID;
    private final EntityBehaviorType behaviorType;
    private final boolean value;
    @Nullable
    private final BlockPos pos;

    public EntityInteractionPacket(int EntityID, EntityBehaviorType behaviorType, boolean value){
        this(EntityID, behaviorType, value, null);
    }

    public EntityInteractionPacket(int EntityID, EntityBehaviorType behaviorType, boolean value, @Nullable BlockPos pos) {
        this.EntityID = EntityID;
        this.behaviorType = behaviorType;
        this.value = value;
        this.pos = pos;
    }

    public static EntityInteractionPacket decode (final PacketBuffer buffer){
        final int ID = buffer.readInt();
        final EntityBehaviorType BehaviorType = buffer.readEnum(EntityBehaviorType.class);
        final boolean value = buffer.readBoolean();
        return new EntityInteractionPacket(ID, BehaviorType, value);
    }

    public static void encode(final EntityInteractionPacket msg, final PacketBuffer buffer){
        buffer.writeInt(msg.EntityID);
        buffer.writeEnum(msg.behaviorType);
        buffer.writeBoolean(msg.value);
    }

    public static void handle(final EntityInteractionPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                final ServerWorld world = Objects.requireNonNull(ctx.get().getSender()).getLevel();
                Entity entity = world.getEntity(msg.EntityID);
                if(entity instanceof AbstractEntityCompanion){
                    switch(msg.behaviorType){
                        case SIT:
                            ((AbstractEntityCompanion) entity).setOrderedToSit(msg.value);
                            break;
                        case HOMEMODE:
                            ((AbstractEntityCompanion) entity).setFreeRoaming(msg.value);
                            break;
                        case ITEMPICKUP:
                            ((AbstractEntityCompanion) entity).setPickupItem(msg.value);
                            break;
                        case ATTACK:
                            ((AbstractEntityCompanion) entity).setShouldAttackFirst(msg.value);
                            break;
                        case PAT:
                            ((AbstractEntityCompanion) entity).beingpatted();
                            break;
                        case ECCI:
                            ((AbstractEntityCompanion) entity).startqinteraction();
                            break;
                        case STOP_RIDING:
                            entity.stopRiding();
                            if(msg.pos != null) {
                                if (((AbstractEntityCompanion) entity).isCriticallyInjured() || world.isNight()) {
                                    BlockUtil.getAvailableBedPos(world, msg.pos, (LivingEntity) entity).ifPresent(((AbstractEntityCompanion) entity)::startSleeping);
                                }
                            }
                        case HEAL:

                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum EntityBehaviorType {
        SIT,
        HOMEMODE,
        PAT,
        HEAL,
        ITEMPICKUP,
        ECCI,
        ATTACK,
        STOP_RIDING
    }
}
