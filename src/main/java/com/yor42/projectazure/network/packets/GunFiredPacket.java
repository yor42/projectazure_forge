package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;

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
                Hand hand = message.offHand ? Hand.OFF_HAND : Hand.MAIN_HAND;
                ItemStack heldStack = playerEntity.getHeldItem(hand);

                if (!heldStack.isEmpty() && heldStack.getItem() instanceof ItemGunBase) {
                    if(!message.offHand) {
                        if (mainDelay <= 0) {
                            boolean shouldDoReloadAnim = ((ItemGunBase) heldStack.getItem()).shootGun(heldStack, playerEntity.getEntityWorld(), playerEntity, message.isZooming, hand, null);
                            if(shouldDoReloadAnim || getAmmo(heldStack)>0) {
                                Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new DoGunAnimationPacket(message.offHand, message.isZooming, playerEntity.getEntityId(), shouldDoReloadAnim));
                            }
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static short getAmmo(ItemStack stack){
        CompoundNBT compound = stack.getOrCreateTag();
        return compound.getShort("ammo");
    }



}
