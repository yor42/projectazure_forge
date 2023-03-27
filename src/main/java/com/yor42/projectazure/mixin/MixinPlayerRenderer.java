package com.yor42.projectazure.mixin;

import com.yor42.projectazure.client.renderer.layer.EntityOntheBackLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public MixinPlayerRenderer(EntityRendererProvider.Context  p_i50965_1_, PlayerModel<AbstractClientPlayer> p_i50965_2_, float p_i50965_3_) {
        super(p_i50965_1_, p_i50965_2_, p_i50965_3_);
    }

    @Inject(method = "<init>", at=@At("TAIL"))
    private void onConstructor(EntityRendererProvider.Context pContext, boolean pUseSlimModel, CallbackInfo ci){
        this.addLayer(new EntityOntheBackLayer<>(this));
    }
}
