package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.AttackTargetTask;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.SwordItem;
import net.minecraft.world.server.ServerWorld;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionAttackTargetTask extends AttackTargetTask {
    public CompanionAttackTargetTask(int p_i231523_1_) {
        super(p_i231523_1_);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, MobEntity p_212832_2_) {

        if(p_212832_2_ instanceof IMeleeAttacker && p_212832_2_.getBrain().getMemory(ATTACK_TARGET).map(((IMeleeAttacker) p_212832_2_)::shouldUseNonVanillaAttack).orElse(false)){
            return false;
        }

        return !isHoldingUsableProjectileWeapon(p_212832_2_) && super.checkExtraStartConditions(p_212832_1_, p_212832_2_)&& p_212832_2_.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() instanceof SwordItem;
    }

    private boolean isHoldingUsableProjectileWeapon(MobEntity p_233921_1_) {
        return p_233921_1_.isHolding((p_233922_1_) -> p_233922_1_ instanceof GunItem);
    }

    private LivingEntity getAttackTarget(MobEntity p_233923_1_) {
        return p_233923_1_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
