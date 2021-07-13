package com.yor42.projectazure.network.packets;

import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Supplier;

public class DoGunAnimationPacket {

    private boolean isOffHand, isZooming, shouldPlayReloadAnimation;
    private int PlayerID;

    public DoGunAnimationPacket(boolean isOffHand, boolean isZooming, int playerID, boolean shouldPlayReloadAnimation){
        this.isOffHand = isOffHand;
        this.isZooming = isZooming;
        this.PlayerID = playerID;
        this.shouldPlayReloadAnimation = shouldPlayReloadAnimation;
    }

    public static DoGunAnimationPacket decode(PacketBuffer buffer){
        final boolean isOffHand = buffer.readBoolean();
        final boolean isZooming = buffer.readBoolean();
        final int playerID = buffer.readInt();
        final boolean shouldPlayReloadanim = buffer.readBoolean();

        return new DoGunAnimationPacket(isOffHand, isZooming, playerID, shouldPlayReloadanim);
    }

    public static void encode(final DoGunAnimationPacket message, final PacketBuffer buffer){
        buffer.writeBoolean(message.isOffHand);
        buffer.writeBoolean(message.isZooming);
        buffer.writeInt(message.PlayerID);
        buffer.writeBoolean(message.shouldPlayReloadAnimation);
    }

    public static void handle(final DoGunAnimationPacket message, final Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
            World clientWorld = ClientProxy.getClientWorld();;
            Entity entity =  clientWorld.getEntityByID(message.PlayerID);

            if(entity instanceof PlayerEntity){
                PlayerEntity playerEntity = (PlayerEntity) entity;

                ItemStack mainstck = playerEntity.getHeldItemMainhand();
                ItemStack offStack = playerEntity.getHeldItemOffhand();
                if(message.isOffHand){
                    if(offStack.getItem() instanceof ItemGunBase){

                        AnimationController controller = GeckoLibUtil.getControllerForStack(((ItemGunBase) offStack.getItem()).getFactory(), offStack, ((ItemGunBase) offStack.getItem()).getFactoryName());
                        ((ItemGunBase) offStack.getItem()).shootGun(offStack, clientWorld, playerEntity, message.isZooming, Hand.OFF_HAND, null);
                        if(message.shouldPlayReloadAnimation) {
                            controller.markNeedsReload();
                            controller.setAnimation(new AnimationBuilder().addAnimation("animation.abydos550.reload", false));
                        }
                        else{
                            controller.markNeedsReload();
                            controller.setAnimation(new AnimationBuilder().addAnimation("animation.abydos550.fire", false));
                        }


                    }
                }
                else{
                    if(mainstck.getItem() instanceof ItemGunBase){
                        AnimationController controller = GeckoLibUtil.getControllerForStack(((ItemGunBase) mainstck.getItem()).getFactory(), mainstck, ((ItemGunBase) mainstck.getItem()).getFactoryName());
                        ((ItemGunBase) mainstck.getItem()).shootGun(mainstck, clientWorld, playerEntity, message.isZooming, Hand.MAIN_HAND, null);

                        if(message.shouldPlayReloadAnimation) {
                            controller.markNeedsReload();
                            controller.setAnimation(new AnimationBuilder().addAnimation("animation.abydos550.reload", false));
                        }
                        else{
                            controller.markNeedsReload();
                            controller.setAnimation(new AnimationBuilder().addAnimation("animation.abydos550.fire", false));
                        }
                    }
                }
            }

        }));
        ctx.get().setPacketHandled(true);
    }



}
