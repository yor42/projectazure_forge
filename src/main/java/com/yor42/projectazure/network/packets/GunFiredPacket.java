package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class GunFiredPacket{

    public boolean isZooming;
    public boolean offHand;

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
                Hand hand = message.offHand ? Hand.OFF_HAND : Hand.MAIN_HAND;
                ItemStack heldStack = playerEntity.getItemInHand(hand);

                if (!heldStack.isEmpty() && heldStack.getItem() instanceof ItemGunBase) {
                    if(!message.offHand) {
                        if (mainDelay <= 0) {
                            boolean shouldDoReloadAnim = ((ItemGunBase) heldStack.getItem()).shootGun(heldStack, playerEntity.getCommandSenderWorld(), playerEntity, message.isZooming, hand, null);
                            Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new DoGunAnimationPacket(message.offHand, message.isZooming, playerEntity.getId(), shouldDoReloadAnim));
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static short getAmmo(ItemStack stack){
        CompoundTag compound = stack.getOrCreateTag();
        return compound.getShort("ammo");
    }



}
