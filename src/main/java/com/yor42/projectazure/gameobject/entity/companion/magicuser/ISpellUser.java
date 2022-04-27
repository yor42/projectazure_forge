package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Level;
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
    boolean shouldUseSpell();
    void setSpellDelay(int delay);

    void ShootProjectile(Level world, @Nonnull LivingEntity target);
    void StartShootingEntityUsingSpell(LivingEntity target);
}
