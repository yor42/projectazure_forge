package com.yor42.projectazure.mixin;

import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WaterBoundPathNavigation.class)
public interface SwimmerPathNavigatorAccess {

    @Accessor
    boolean getAllowBreaching();

}
