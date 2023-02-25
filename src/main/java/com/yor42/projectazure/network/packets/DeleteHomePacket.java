package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.HOME;

public class DeleteHomePacket {
    private final int entityID;

    public DeleteHomePacket(int entityID){
        this.entityID = entityID;
    }

    public static DeleteHomePacket decode (final PacketBuffer buffer){
        final int id = buffer.readInt();
        return new DeleteHomePacket(id);
    }

    public static void encode(final DeleteHomePacket msg, final PacketBuffer buffer){
        buffer.writeInt(msg.entityID);
    }

    public static void handle(final DeleteHomePacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity!= null) {
                final ServerWorld world = playerEntity.getLevel();
                Entity ety = world.getEntity(msg.entityID);
                if(ety instanceof AbstractEntityCompanion){
                    ((AbstractEntityCompanion) ety).releasePoi(MemoryModuleType.HOME);
                    ((AbstractEntityCompanion) ety).getBrain().eraseMemory(HOME);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
