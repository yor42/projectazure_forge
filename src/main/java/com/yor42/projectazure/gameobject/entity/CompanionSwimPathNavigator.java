package com.yor42.projectazure.gameobject.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;

public class CompanionSwimPathNavigator extends WaterBoundPathNavigation {
    public CompanionSwimPathNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
        this.nodeEvaluator = new CompanionSwimNodeProcessor(false);
    }
}
