package com.yor42.projectazure.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;

import java.util.function.Supplier;

public class SyncGeoGunAnimationPacket {

    private final ItemStack stack;
    private final int stackid, animid;

    public SyncGeoGunAnimationPacket(ItemStack stack, int stackid, int animid){
        this.stack = stack;
        this.stackid = stackid;
        this.animid = animid;
    }

    public static SyncGeoGunAnimationPacket decode (final FriendlyByteBuf buffer){
        return new SyncGeoGunAnimationPacket(buffer.readItem(), buffer.readInt(), buffer.readInt());
    }

    public static void encode(final SyncGeoGunAnimationPacket msg, final FriendlyByteBuf buffer){
        buffer.writeItem(msg.stack);
        buffer.writeInt(msg.stackid);
        buffer.writeInt(msg.animid);
    }


    public static void handle(final SyncGeoGunAnimationPacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final ServerPlayer playerEntity = ctx.get().getSender();
            if(msg.stack.getItem() instanceof ISyncable syncable) {
                GeckoLibNetwork.syncAnimation(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), syncable, msg.stackid, msg.animid);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
