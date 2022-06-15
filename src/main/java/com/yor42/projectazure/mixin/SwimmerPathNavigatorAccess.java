package com.yor42.projectazure.mixin;

import net.minecraft.pathfinding.SwimmerPathNavigator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SwimmerPathNavigator.class)
public interface SwimmerPathNavigatorAccess {

    @Accessor
    boolean getAllowBreaching();

}
