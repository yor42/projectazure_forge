package com.yor42.projectazure.gameobject.entity;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Iterator;

public class CompanionGroundPathNavigator extends GroundPathNavigator {

    protected boolean shouldAvoidSun;
    private final AbstractEntityCompanion companion;

    public CompanionGroundPathNavigator(AbstractEntityCompanion entity, World world) {
        super(entity, world);
        this.companion = entity;
    }

    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new WalkNodeProcessor();
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
