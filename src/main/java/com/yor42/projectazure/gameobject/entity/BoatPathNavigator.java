package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.World;

public class BoatPathNavigator extends SwimmerPathNavigator {
    public BoatPathNavigator(MobEntity p_i45873_1_, World p_i45873_2_) {
        super(p_i45873_1_, p_i45873_2_);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }
}
