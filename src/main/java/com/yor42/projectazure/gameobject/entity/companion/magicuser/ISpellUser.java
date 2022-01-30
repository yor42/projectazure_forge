package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public interface ISpellUser {

    /*
    Delay between Each fire
     */
    int getInitialSpellDelay();
    /*
    Delay of Pre animation before actually firing projectile
     */
    int getProjectilePreAnimationDelay();
    Hand getSpellUsingHand();
    boolean shouldUseSpell();
    void setSpellDelay(int delay);

    void ShootProjectile(World world, @Nonnull LivingEntity target);
    void StartShootingEntityUsingSpell(LivingEntity target);
}
