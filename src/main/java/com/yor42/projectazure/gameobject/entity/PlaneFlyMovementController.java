package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.FlyingMovementController;

public class PlaneFlyMovementController extends FlyingMovementController {
    private final int maxTurn;
    public PlaneFlyMovementController(MobEntity p_i225710_1_, int p_i225710_2_, boolean p_i225710_3_) {
        super(p_i225710_1_, p_i225710_2_, p_i225710_3_);
        this.maxTurn = p_i225710_2_;
    }


}
