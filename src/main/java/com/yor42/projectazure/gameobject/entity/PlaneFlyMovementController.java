package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class PlaneFlyMovementController extends FlyingMovementController {
    private final int maxTurn;
    public PlaneFlyMovementController(MobEntity p_i225710_1_, int p_i225710_2_, boolean p_i225710_3_) {
        super(p_i225710_1_, p_i225710_2_, p_i225710_3_);
        this.maxTurn = p_i225710_2_;
    }


}
