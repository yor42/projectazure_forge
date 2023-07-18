package com.yor42.projectazure.mixin;

import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.events.ChargeFireHandler;
import com.yor42.projectazure.interfaces.IChargeFire;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShootingHandler.class)
public abstract class MixinShootingHandler {

    @Inject(method = "fire", at = @At("HEAD"), remap = false, cancellable = true)
    private void onFire(Player player, ItemStack heldItem, CallbackInfo ci) {
        if (this.magError(player, heldItem)) {
            ci.cancel();
        }

        if (heldItem.getItem() instanceof IChargeFire) {
            ChargeFireHandler handler = ChargeFireHandler.getInstance();
            if(!handler.shouldfire()){
                ci.cancel();
            }
        }
    }
    @Shadow(remap = false)
    private boolean magError(Player player, ItemStack heldItem){
        return false;
    };

}
