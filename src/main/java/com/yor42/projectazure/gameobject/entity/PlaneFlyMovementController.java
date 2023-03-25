package com.yor42.projectazure.gameobject.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;

public class PlaneFlyMovementController extends FlyingMoveControl {
    private final int maxTurn;
    public PlaneFlyMovementController(Mob p_i225710_1_, int p_i225710_2_, boolean p_i225710_3_) {
        super(p_i225710_1_, p_i225710_2_, p_i225710_3_);
        this.maxTurn = p_i225710_2_;
    }


}
