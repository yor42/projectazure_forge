package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.pathfinding.CompanionWalkerNodeProcessor;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class CompanionGroundPathNavigator extends GroundPathNavigation {

    protected boolean shouldAvoidSun;
    private final AbstractEntityCompanion companion;

    public CompanionGroundPathNavigator(AbstractEntityCompanion entity, Level world) {
        super(entity, world);
        this.companion = entity;
    }

    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new CompanionWalkerNodeProcessor();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanOpenDoors(true);
        return new PathFinder(this.nodeEvaluator, p_179679_1_);
    }

}
