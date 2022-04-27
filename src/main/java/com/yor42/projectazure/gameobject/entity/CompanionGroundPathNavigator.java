package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.pathfinding.CompanionWalkerNodeProcessor;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.Level;
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

    protected boolean hasValidPathType(PathNodeType p_230287_1_) {
        if (p_230287_1_ == PathNodeType.WATER) {
            return true;
        } else if (p_230287_1_ == PathNodeType.LAVA) {
            return false;
        } else {
            return p_230287_1_ != PathNodeType.OPEN;
        }
    }
}
