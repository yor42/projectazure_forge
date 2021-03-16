package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Supplier;

public class GunFiredPacket{

    public boolean isZooming=false;
    public boolean offHand=false;

    public GunFiredPacket(boolean isZooming, boolean isOffHand){
        this.isZooming = isZooming;
        this.offHand = isOffHand;
    }

    public static GunFiredPacket decode(PacketBuffer buffer){
        final boolean isZooming = buffer.readBoolean();
        final boolean isOffHand = buffer.readBoolean();

        return new GunFiredPacket(isZooming, isOffHand);
    }

    public static void encode(final GunFiredPacket message, final PacketBuffer buffer){
        buffer.writeBoolean(message.isZooming);
        buffer.writeBoolean(message.offHand);
    }

    public static void handle(final GunFiredPacket message, final Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            final PlayerEntity playerEntity = ctx.get().getSender();
            if(playerEntity != null) {
                ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(playerEntity);
                int mainDelay = capability.getMainHandFireDelay();
                int offDelay = capability.getOffHandFireDelay();
                ItemStack heldStack = playerEntity.getHeldItem(message.offHand ? Hand.OFF_HAND : Hand.MAIN_HAND);

                if (!heldStack.isEmpty() && heldStack.getItem() instanceof ItemGunBase) {
                    if(!message.offHand) {
                        if (mainDelay <= 0) {
                            Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new DoGunAnimationPacket(message.offHand, message.isZooming, playerEntity.getEntityId(), ((ItemGunBase) heldStack.getItem()).shootGun(heldStack, playerEntity.getEntityWorld(), playerEntity, message.isZooming, message.offHand ? Hand.OFF_HAND : Hand.MAIN_HAND, null)));
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }



}
