package com.yor42.projectazure.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    @Accessor
    PostChain getPostEffect();

    @Accessor
    void setPostEffect(PostChain effect);

    @Accessor
    boolean getEffectActive();

    @Accessor
    void setEffectActive(boolean value);

}
