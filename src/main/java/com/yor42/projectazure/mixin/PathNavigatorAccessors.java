package com.yor42.projectazure.mixin;

import net.minecraft.pathfinding.PathNavigator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PathNavigator.class)
public interface PathNavigatorAccessors {
    @Accessor
    double getSpeedModifier();
}
