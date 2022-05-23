package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionUseShieldTask extends Task<AbstractEntityCompanion> {
    public CompanionUseShieldTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion companion) {
        return companion.getOffhandItem().getItem().isShield(companion.getOffhandItem(), companion) && raiseShield(companion) && companion.getShieldCoolDown() == 0;
    }

    @Override
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        boolean bool = this.checkExtraStartConditions(p_212834_1_, p_212834_2_);

        if(!bool){
            this.doStop(p_212834_1_, p_212834_2_, p_212834_3_);
        }

        return bool;
    }

    @Override
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion companion, long p_212831_3_) {
        if (companion.getOffhandItem().getItem().isShield(companion.getOffhandItem(), companion))
            companion.startUsingItem(Hand.OFF_HAND);
    }

    protected boolean raiseShield(AbstractEntityCompanion companion) {
        return companion.getBrain().getMemory(ATTACK_TARGET).map((target)->{
            if(companion.isSailing()){
                return false;
            }

            if (companion.getShieldCoolDown() == 0) {
                boolean ranged = companion.getMainHandItem().getItem() instanceof CrossbowItem || companion.getMainHandItem().getItem() instanceof BowItem || companion.getMainHandItem().getItem() instanceof ItemGunBase;
                return companion.distanceTo(target) <= 4.0D || target instanceof CreeperEntity || target instanceof IRangedAttackMob && target.distanceTo(companion) >= 5.0D && !ranged || target instanceof RavagerEntity;
            }
            return false;
        }).orElse(false);
    }
}
