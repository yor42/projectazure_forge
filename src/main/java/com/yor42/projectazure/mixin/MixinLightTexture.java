package com.yor42.projectazure.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yor42.projectazure.gameobject.items.ItemNightVisionHelmet;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightTexture.class)
public class MixinLightTexture {

    @ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
    private boolean shouldRenderNightVision(boolean original){

        boolean val = false;

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if(player != null && minecraft.options.getCameraType().isFirstPerson()) {

            ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack NVG = ItemStack.EMPTY;

            if (headStack.getItem() instanceof ItemNightVisionHelmet) {
                NVG = headStack;
            } else if (CompatibilityUtils.isCurioLoaded()) {
                NVG = CuriosCompat.getCurioItemStack(player, "head", (item) -> item.getItem() instanceof ItemNightVisionHelmet);
            }

            if(ItemStackUtils.isOn(NVG)){
                val = true;
            }

        }

        return original || val;
    }

}
