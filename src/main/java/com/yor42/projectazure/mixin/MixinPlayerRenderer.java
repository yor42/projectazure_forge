package com.yor42.projectazure.mixin;

import com.yor42.projectazure.client.renderer.layer.EntityOntheBackLayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.model.PlayerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public MixinPlayerRenderer(EntityRenderDispatcher p_i50965_1_, PlayerModel<AbstractClientPlayer> p_i50965_2_, float p_i50965_3_) {
        super(p_i50965_1_, p_i50965_2_, p_i50965_3_);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererManager;Z)V", at=@At("TAIL"))
    private void onConstructor(EntityRenderDispatcher p_i46103_1_, boolean p_i46103_2_, CallbackInfo ci){
        this.addLayer(new EntityOntheBackLayer<>(this));
    }
}
