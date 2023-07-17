package com.yor42.projectazure.mixin;

import com.tac.guns.client.handler.ShootingHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShootingHandler.class)
public interface ShootingHandlerAccessor {

    @Accessor(remap = false)
    int getBurstCooldown();

}
