package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.Level;

public class CompanionSwimPathNavigator extends SwimmerPathNavigator {
    public CompanionSwimPathNavigator(MobEntity entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
        this.nodeEvaluator = new CompanionSwimNodeProcessor(false);
    }
}
