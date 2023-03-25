package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.SwordItem;
import net.minecraft.server.level.ServerLevel;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET;


public class CompanionAttackTargetTask extends Behavior<AbstractEntityCompanion> {
    public CompanionAttackTargetTask(int p_i231523_1_) {
        super(p_i231523_1_);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, Mob p_212832_2_) {

        if(p_212832_2_ instanceof IMeleeAttacker && p_212832_2_.getBrain().getMemory(ATTACK_TARGET).map(((IMeleeAttacker) p_212832_2_)::shouldUseNonVanillaAttack).orElse(false)){
            return false;
        }

        return !isHoldingUsableProjectileWeapon(p_212832_2_) && super.checkExtraStartConditions(p_212832_1_, p_212832_2_)&& p_212832_2_.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem;
    }

    private boolean isHoldingUsableProjectileWeapon(Mob p_233921_1_) {
        return p_233921_1_.isHolding((p_233922_1_) -> p_233922_1_.getItem() instanceof GunItem);
    }

    private LivingEntity getAttackTarget(Mob p_233923_1_) {
        return p_233923_1_.getBrain().getMemory(ATTACK_TARGET).get();
    }
}
