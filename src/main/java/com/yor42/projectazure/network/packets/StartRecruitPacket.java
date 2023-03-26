package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.blocks.tileentity.AbstractTileEntityGacha;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StartRecruitPacket {
    private final BlockPos pos;
    public StartRecruitPacket(BlockPos pos){
        this.pos = pos;
    }

    public static StartRecruitPacket decode (final FriendlyByteBuf buffer){
        final BlockPos pos = buffer.readBlockPos();
        return new StartRecruitPacket(pos);
    }

    public static void encode(final StartRecruitPacket msg, final FriendlyByteBuf buffer){
        buffer.writeBlockPos(msg.pos);
    }

    public static void handle(final StartRecruitPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if(sender != null){
                Level world = sender.getLevel();
                BlockEntity TE = world.getBlockEntity(msg.pos);
                if(TE instanceof AbstractTileEntityGacha){
                    ((AbstractTileEntityGacha) TE).StartMachine(sender);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
