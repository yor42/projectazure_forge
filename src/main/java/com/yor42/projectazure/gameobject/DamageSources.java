package com.yor42.projectazure.gameobject;

import net.minecraft.util.DamageSource;

public class DamageSources {
    public static final DamageSource DAMAGE_GUN = (new DamageSource("canon_shot_generic"));

    public static final DamageSource SHIP_FIRE = new DamageSource("ship_fire").setFireDamage();
    public static final DamageSource TORPEDO = new DamageSource("torpedo").setExplosion();

    public static final DamageSource PLANE_GUN = new DamageSource("plane_gun").setProjectile();

    //Uhhhhhhh
    public static final DamageSource BONK = new DamageSource("bonk").setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode();
}
