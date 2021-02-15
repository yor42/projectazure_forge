package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;

public interface IShipRangedAttack {

    void AttackUsingCannon(LivingEntity target, float distanceFactor);
    boolean canUseAmmo(enums.AmmoCategory types);
    boolean isCanonReady();
    //void AttackUsingTorpedo(LivingEntity target, float distanceFactor);

}
