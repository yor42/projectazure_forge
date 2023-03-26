package com.yor42.projectazure.interfaces;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public interface ISpellUser {

    /*
    Delay between Each fire
     */
    int getInitialSpellDelay();
    /*
    Delay of Pre animation before actually firing projectile
     */
    int getProjectilePreAnimationDelay();
    InteractionHand getSpellUsingHand();
    boolean shouldUseSpell(LivingEntity target);

    default int SpellCooldown(){
        return 0;
    }

    void ShootProjectile(Level world, @Nonnull LivingEntity target);
    default void StartSpellAttack(LivingEntity target){}
}
