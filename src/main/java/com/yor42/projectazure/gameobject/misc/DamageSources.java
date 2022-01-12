package com.yor42.projectazure.gameobject.misc;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityArtsProjectile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

import javax.annotation.Nullable;

public class DamageSources {
    public static final DamageSource DAMAGE_CANNON = new DamageSource("canon_shot_generic").setProjectile().setDamageIsAbsolute();

    public static final DamageSource SHIP_FIRE = new DamageSource("ship_fire").setFireDamage();
    public static final DamageSource TORPEDO = new DamageSource("torpedo").setExplosion();

    public static final DamageSource ARTS = new DamageSourcesWithRandomMessages("arts", 5).setMagicDamage().setDamageBypassesArmor().setProjectile();

    public static final DamageSource Revenge = new DamageSourcesWithRandomMessages("revenge", 5);

    public static final DamageSource ACUTE_ORIPATHY = new DamageSourcesWithRandomMessages("ate_originium_prime", 5).setDamageBypassesArmor().setDamageIsAbsolute();

    public static final DamageSource PLANE_GUN = new DamageSource("plane_gun").setProjectile();

    public static final DamageSource CLAYMORE = new DamageSourcesWithRandomMessages("claymore", 3).setDamageBypassesArmor();

    public static DamageSource causeGunDamage(EntityProjectileBullet bullet, @Nullable Entity indirectEntityIn) {
        return (new IndirectEntityDamageSource("gun_bullet", bullet, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeRevengeDamage(AbstractEntityCompanion companion) {
        return (new PACompanionAttackDamage("revenge", companion, 5));
    }

    public static DamageSource causeArtsFireDamage(AbstractEntityCompanion companion) {
        return (new PACompanionAttackDamage("fire_arts", companion, 5).setFireDamage().setMagicDamage());
    }

    public static DamageSource causeCannonDamage(EntityCannonPelllet cannonPelllet, @Nullable Entity indirectEntityIn) {
        return (new IndirectEntityDamageSource("canon_shot_generic", cannonPelllet, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeArtsDamage(EntityArtsProjectile projectile, @Nullable Entity indirectEntityIn) {
        return (new IndirectDamageSourcewithRandomMessages("arts_projectile", projectile, indirectEntityIn, 5)).setProjectile().setMagicDamage().setDamageBypassesArmor();
    }

    public static DamageSource causeTorpedoDamage(EntityProjectileTorpedo torpedo, @Nullable Entity indirectEntityIn) {
        return (new IndirectEntityDamageSource("torpedo", torpedo, indirectEntityIn)).setProjectile().setExplosion();
    }

    //Uhhhhhhh
    public static final DamageSource BONK = new DamageSource("bonk").setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode();
}
