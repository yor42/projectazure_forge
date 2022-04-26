package com.yor42.projectazure.gameobject.entity.companion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public interface IMeleeAttacker {
    /*
    Delay between Each Attack
     */
    int getInitialMeleeAttackDelay();
    /*
    Delay of Pre animation before Actual Damage Deals

    FAQ
    Q1. Why arraylist?
    A1. Because some entity apparently deals damage multiple times each attack.
     */
    ArrayList<Integer> getAttackDamageDelay();

    ArrayList<Item> getTalentedWeaponList();

    boolean hasMeleeItem();

    default Hand getNonVanillaMeleeAttackHand(){
        return Hand.MAIN_HAND;
    }

    float getAttackRange(boolean isUsingTalentedWeapon);

    boolean shouldUseNonVanillaAttack(LivingEntity target);
    default boolean isTalentedWeaponinMainHand(){
        return isTalentedWeapon(getMainHandItem());
    }

    default boolean isDuelWielding(){
        return isTalentedWeapon(this.getMainHandItem()) && isTalentedWeapon(getOffhandItem());
    }

    ItemStack getMainHandItem();

    ItemStack getOffhandItem();

    default boolean isTalentedWeapon(ItemStack stack){
        return getTalentedWeaponList().contains(stack.getItem());
    }

    default ArrayList<Integer> getMeleeAnimationAudioCueDelay(){
        return new ArrayList<>();
    }

    void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount);
    default float getAttackSpeedModifier(boolean isUsingTalentedWeapon){
        return isUsingTalentedWeapon? 1:1.2F;
    }
    void StartMeleeAttackingEntity();
}
