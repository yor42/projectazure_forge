package com.yor42.projectazure.mixin;

import com.yor42.projectazure.events.ForgeBusEventHandlerClient;
import com.yor42.projectazure.gameobject.items.ItemNightVisionHelmet;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow
    private PostChain postEffect;

    @Shadow public abstract void tick();

    @Shadow public abstract void render(float pPartialTicks, long pNanoTime, boolean pRenderLevel);

    @Inject(method = "loadEffect", at = @At("HEAD"))
    private void onLoadEffect(ResourceLocation pResourceLocation, CallbackInfo ci){
        ForgeBusEventHandlerClient.activeShader = pResourceLocation;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(float pPartialTicks, long pNanoTime, boolean pRenderLevel, CallbackInfo ci){
        if(this.postEffect == null){
            ForgeBusEventHandlerClient.activeShader = null;
        }
    }

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void onNightVisionScale(LivingEntity pLivingEntity, float pNanoTime, CallbackInfoReturnable<Float> cir){
        ItemStack headStack = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack NVG = ItemStack.EMPTY;

        if (headStack.getItem() instanceof ItemNightVisionHelmet){
            NVG = headStack;
        }
        else if(CompatibilityUtils.isCurioLoaded()){
            NVG = CuriosCompat.getCurioItemStack(pLivingEntity, "head", (item)->item.getItem() instanceof ItemNightVisionHelmet);
        }

        if (ItemStackUtils.isOn(NVG)) {
            cir.setReturnValue(1.0F);
        }
    }

}
