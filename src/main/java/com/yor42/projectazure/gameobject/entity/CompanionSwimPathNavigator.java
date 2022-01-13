package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.World;

public class CompanionSwimPathNavigator extends SwimmerPathNavigator {
    public CompanionSwimPathNavigator(MobEntity entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
        this.nodeEvaluator = new CompanionSwimNodeProcessor(false);
    }
}
