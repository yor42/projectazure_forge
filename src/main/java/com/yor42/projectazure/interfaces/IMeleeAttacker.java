package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.ArrayList;

public interface IMeleeAttacker {
    /*
    Delay between Each Attack
     */
    int MeleeAttackAnimationLength();
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

    default boolean shouldUseNonVanillaAttack(LivingEntity target){
        return this.hasMeleeItem();
    }

    default boolean isTalentedWeaponinMainHand(){
        if(this instanceof AbstractEntityCompanion) {
        return isTalentedWeapon(((AbstractEntityCompanion)this).getMainHandItem());
        }
        return false;
    }

    default boolean isDuelWielding(){
        if(this instanceof AbstractEntityCompanion) {
            return isTalentedWeapon(((AbstractEntityCompanion)this).getMainHandItem()) && isTalentedWeapon(((AbstractEntityCompanion)this).getOffhandItem());
        }
        return false;
    }

    default boolean isTalentedWeapon(ItemStack stack){
        return getTalentedWeaponList().contains(stack.getItem());
    }

    default ArrayList<Integer> MeleeAttackAudioCue(){
        return new ArrayList<>();
    }

    void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount);
    default float getAttackSpeedModifier(boolean isUsingTalentedWeapon){
        return isUsingTalentedWeapon? 1:1.2F;
    }
    default void StartMeleeAttackingEntity(){}
}
