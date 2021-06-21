package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.blocks.tileentity.AbstractTileEntityGacha;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import com.yor42.projectazure.network.serverEvents;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StartRecruitPacket {
    private final BlockPos pos;
    public StartRecruitPacket(BlockPos pos){
        this.pos = pos;
    }

    public static StartRecruitPacket decode (final PacketBuffer buffer){
        final BlockPos pos = buffer.readBlockPos();
        return new StartRecruitPacket(pos);
    }

    public static void encode(final StartRecruitPacket msg, final PacketBuffer buffer){
        buffer.writeBlockPos(msg.pos);
    }

    public static void handle(final StartRecruitPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            if(sender != null){
                World world = sender.getServerWorld();
                TileEntity TE = world.getTileEntity(msg.pos);
                if(TE instanceof AbstractTileEntityGacha){
                    ((AbstractTileEntityGacha) TE).StartMachine(sender);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
