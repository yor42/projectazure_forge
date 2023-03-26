package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class EntityInteractionPacket {

    private final int EntityID;
    private final EntityBehaviorType behaviorType;
    private final boolean value;
    @Nullable
    private final BlockPos pos;
    private final int playerUUID;

    public EntityInteractionPacket(int EntityID, EntityBehaviorType behaviorType, boolean value){
        this(EntityID, behaviorType, value, null, 0);
    }

    public EntityInteractionPacket(int EntityID, EntityBehaviorType behaviorType, boolean value, int playerUUID){
        this(EntityID, behaviorType, value, null, playerUUID);
    }

    public EntityInteractionPacket(int EntityID, EntityBehaviorType behaviorType, boolean value, @Nullable BlockPos pos, int playerUUID) {
        this.EntityID = EntityID;
        this.behaviorType = behaviorType;
        this.value = value;
        this.pos = pos;
        this.playerUUID = playerUUID;
    }

    public static EntityInteractionPacket decode (final FriendlyByteBuf buffer){
        final int ID = buffer.readInt();
        final EntityBehaviorType BehaviorType = buffer.readEnum(EntityBehaviorType.class);
        final boolean value = buffer.readBoolean();
        final int playerUUID = buffer.readInt();
        final BlockPos pos = buffer.readBoolean()? buffer.readBlockPos(): null;
        return new EntityInteractionPacket(ID, BehaviorType, value, pos, playerUUID);
    }

    public static void encode(final EntityInteractionPacket msg, final FriendlyByteBuf buffer){
        buffer.writeInt(msg.EntityID);
        buffer.writeEnum(msg.behaviorType);
        buffer.writeBoolean(msg.value);
        buffer.writeInt(msg.playerUUID);
        if(msg.pos != null){
            buffer.writeBoolean(true);
            buffer.writeBlockPos(msg.pos);
        }
        else{
            buffer.writeBoolean(false);
        }
    }

    public static void handle(final EntityInteractionPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                final ServerLevel world = Objects.requireNonNull(ctx.get().getSender()).getLevel();
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
                            ((AbstractEntityCompanion) entity).beingpatted((Player) world.getEntity(msg.playerUUID));
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
        SIT(Boolean.class),
        HOMEMODE(BlockPos.class),
        PAT(Integer.class),
        HEAL(Boolean.class),
        ITEMPICKUP(Boolean.class),
        ECCI(Integer.class),
        ATTACK(Boolean.class),
        STOP_RIDING(Integer.class);

        private final Class<?> ValueType;

        EntityBehaviorType(Class<?> ValueType){
            this.ValueType = ValueType;
        }
    }
}
