package com.yor42.projectazure.gameobject.entity.companion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

import java.util.ArrayList;

public interface IMeleeAttacker {
    /*
    Delay between Each Attack
     */
    int getInitialAttackDelay();
    /*
    Delay of Pre animation before Actual Damage Deals

    FAQ
    Q1. Why arraylist?
    A1. Because some entity apparently deals damage multiple times each attack.
     */
    ArrayList<Integer> getAttackPreAnimationDelay();

    ArrayList<Item> getTalentedWeaponList();

    boolean hasMeleeItem();

    float getAttackRange(boolean isUsingTalentedWeapon);

    boolean shouldUseNonVanillaAttack();
    boolean isUsingTalentedWeapon();

    void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount);
    float getAttackSpeedModifier(boolean isUsingTalentedWeapon);
    void StartMeleeAttackingEntity();
}