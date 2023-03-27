package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DeleteHomePacket{

    private final int entityID;

    public DeleteHomePacket(int entityID){
        this.entityID = entityID;
    }

    public static DeleteHomePacket decode (final FriendlyByteBuf buffer){
        final int id = buffer.readInt();
        return new DeleteHomePacket(id);
    }

    public static void encode(final DeleteHomePacket msg, final FriendlyByteBuf buffer){
        buffer.writeInt(msg.entityID);
    }

    public static void handle(final DeleteHomePacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();
            if(playerEntity!= null) {
                final ServerLevel world = playerEntity.getLevel();
                Entity ety = world.getEntity(msg.entityID);
                if(ety instanceof AbstractEntityCompanion){
                    ((AbstractEntityCompanion) ety).releasePoi(MemoryModuleType.HOME);
                    ((AbstractEntityCompanion) ety).getBrain().eraseMemory(MemoryModuleType.HOME);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
