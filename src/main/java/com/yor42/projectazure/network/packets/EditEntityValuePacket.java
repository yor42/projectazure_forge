package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class EditEntityValuePacket {
    private final int EntityID;
    private final ValueType valueType;
    private final EditType editType;
    private final float value;
    public EditEntityValuePacket(int EntityID, ValueType valueType, EditType EditType, float value){
        this.EntityID = EntityID;
        this.valueType = valueType;
        this.editType = EditType;
        this.value = value;
    }

    public static EditEntityValuePacket decode (final PacketBuffer buffer){
        final int ID = buffer.readInt();
        final EditEntityValuePacket.ValueType ValueType = buffer.readEnum(EditEntityValuePacket.ValueType.class);
        final EditType editType = buffer.readEnum(EditType.class);
        final float value = buffer.readFloat();
        return new EditEntityValuePacket(ID, ValueType, editType, value);
    }

    public static void encode(final EditEntityValuePacket msg, final PacketBuffer buffer){
        buffer.writeInt(msg.EntityID);
        buffer.writeEnum(msg.valueType);
        buffer.writeEnum(msg.editType);
        buffer.writeFloat(msg.value);
    }

    public static void handle(final EditEntityValuePacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                final ServerWorld world = Objects.requireNonNull(ctx.get().getSender()).getLevel();
                Entity entity = world.getEntity(msg.EntityID);
                if(entity instanceof AbstractEntityCompanion){
                    switch(msg.editType){
                        case ADD:
                            switch(msg.valueType){
                                case AFFECTION:
                                    ((AbstractEntityCompanion) entity).addAffection(msg.value);
                                    break;
                                case MORALE:
                                    ((AbstractEntityCompanion) entity).addMorale(msg.value);
                                    break;
                                case LEVEL:
                                    ((AbstractEntityCompanion) entity).addLevel((int) msg.value);
                                    break;
                            }
                            break;
                        case SET:
                            switch(msg.valueType){
                                case AFFECTION:
                                    ((AbstractEntityCompanion) entity).setAffection(msg.value);
                                    break;
                                case MORALE:
                                    ((AbstractEntityCompanion) entity).setMorale(msg.value);
                                    break;
                                case LEVEL:
                                    ((AbstractEntityCompanion) entity).setLevel((int) msg.value);
                                    break;
                            }
                            break;
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public enum ValueType{
        MORALE,
        AFFECTION,
        LEVEL,
    }

    public enum EditType{
        SET,
        ADD
    }
}
