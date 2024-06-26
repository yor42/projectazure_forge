package com.yor42.projectazure.gameobject.misc;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityArtsProjectile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class DamageSources {
    public static final DamageSource DAMAGE_CANNON = new DamageSource("canon_shot_generic").setProjectile().bypassMagic().bypassInvul();

    public static final DamageSource SHIP_FIRE = new DamageSource("ship_fire").setIsFire();
    public static final DamageSource CAUSAL_BLACKHOLE = new DamageSource("blackhole").bypassArmor();
    public static final DamageSource TORPEDO = new DamageSource("torpedo").setExplosion().bypassInvul();

    public static final DamageSource ACUTE_ORIPATHY = new DamageSourcesWithRandomMessages("ate_originium_prime", 5).bypassArmor().bypassMagic();
    public static final DamageSource FROSTBITE = new DamageSourcesWithRandomMessages("frostbite", 2).bypassArmor().bypassMagic();

    public static final DamageSource PLANE_GUN = new DamageSource("plane_gun").setProjectile();

    public static final DamageSource CLAYMORE = new DamageSourcesWithRandomMessages("claymore", 3).bypassArmor();

    public static DamageSource causeRevengeDamage(AbstractEntityCompanion companion) {
        return (new PACompanionAttackDamage("revenge", companion, 5));
    }

    public static DamageSource knife(Entity p_203096_0_, @Nullable Entity p_203096_1_) {
        return (new IndirectEntityDamageSource("knife", p_203096_0_, p_203096_1_)).setProjectile();
    }

    public static DamageSource causeDefibDamage(Player player) {
        return (new PACompanionAttackDamage("revenge", player, 5).bypassArmor().bypassInvul());
    }

    public static DamageSource causeArtsFireDamage(AbstractEntityCompanion companion) {
        return (new PACompanionAttackDamage("fire_arts", companion, 5).setIsFire().setMagic());
    }

    public static DamageSource causeCannonDamage(EntityCannonPelllet cannonPelllet, @Nullable Entity indirectEntityIn) {
        return (new IndirectEntityDamageSource("canon_shot_generic", cannonPelllet, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeArtsDamage(EntityArtsProjectile projectile, @Nullable Entity indirectEntityIn) {
        return (new IndirectDamageSourcewithRandomMessages("arts_projectile", projectile, indirectEntityIn, 5)).setProjectile().setMagic().bypassArmor();
    }

    public static DamageSource causeTorpedoDamage(EntityProjectileTorpedo torpedo, @Nullable Entity indirectEntityIn) {
        return (new IndirectEntityDamageSource("torpedo", torpedo, indirectEntityIn)).setProjectile().setExplosion();
    }

    //Uhhhhhhh
    public static final DamageSource BONK = new DamageSource("bonk").bypassArmor().bypassMagic().bypassInvul();
}
