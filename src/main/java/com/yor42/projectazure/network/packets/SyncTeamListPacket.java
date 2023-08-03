package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.misc.ProjectAzureWorldSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncTeamListPacket {

    private final CompoundTag entry;
    private final ACTION action;

    public SyncTeamListPacket(CompoundTag compound) {
        this(compound, ACTION.ADD);
    }

    public SyncTeamListPacket(CompoundTag compound, ACTION action) {
        this.entry = compound;
        this.action = action;
    }

    public static SyncTeamListPacket decode (final FriendlyByteBuf buffer){
        final ACTION action = buffer.readEnum(ACTION.class);
        final CompoundTag compound = buffer.readNbt();
        return new SyncTeamListPacket(compound, action);
    }

    public static void encode(final SyncTeamListPacket msg, final FriendlyByteBuf buffer){
        buffer.writeEnum(msg.action);
        buffer.writeNbt(msg.entry);
    }

    public static void handle(final SyncTeamListPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->() ->{
                switch (msg.action) {
                    default:ProjectAzureWorldSavedData.DeserializeandUpdateClientList(msg.entry);break;
                    case ADD_BATCH:ProjectAzureWorldSavedData.DeserializeandUpdateEntireClientList(msg.entry);break;
                    case REMOVE:ProjectAzureWorldSavedData.RemoveEntryFromClientList(msg.entry);break;
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public enum ACTION{
        ADD,
        ADD_BATCH,
        REMOVE
    }

}
