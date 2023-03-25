package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;
import static net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionUseShieldTask extends Behavior<AbstractEntityCompanion> {
    public CompanionUseShieldTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion companion) {
        return companion.getOffhandItem().getItem().isShield(companion.getOffhandItem(), companion) && raiseShield(companion) && companion.getShieldCoolDown() == 0;
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        boolean bool = this.checkExtraStartConditions(p_212834_1_, p_212834_2_);

        if(!bool){
            this.doStop(p_212834_1_, p_212834_2_, p_212834_3_);
        }

        return bool;
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion companion, long p_212831_3_) {
        if (companion.getOffhandItem().getItem().isShield(companion.getOffhandItem(), companion))
            companion.startUsingItem(InteractionHand.OFF_HAND);
    }

    protected boolean raiseShield(AbstractEntityCompanion companion) {
        return companion.getBrain().getMemory(ATTACK_TARGET).map((target)->{
            if(companion.isSailing()){
                return false;
            }

            if (companion.getShieldCoolDown() == 0) {
                boolean ranged = companion.getMainHandItem().getItem() instanceof CrossbowItem || companion.getMainHandItem().getItem() instanceof BowItem || companion.getMainHandItem().getItem() instanceof GunItem;
                return companion.distanceTo(target) <= 4.0D || target instanceof Creeper || target instanceof RangedAttackMob && target.distanceTo(companion) >= 5.0D && !ranged || target instanceof Ravager;
            }
            return false;
        }).orElse(false);
    }
}
