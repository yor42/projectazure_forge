package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionShipRangedAttackTask extends Behavior<AbstractEntityCompanion> {

    private int CannonAttackDelay;
    int torpedoAttackDelay;

    public CompanionShipRangedAttackTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion entityHost) {
        LivingEntity target = getAttackTarget(entityHost);
        if(!entityHost.closerThan(target, entityHost.getCannonRange())){
            return false;
        }

        if(entityHost instanceof EntityKansenBase) {
            if (((EntityKansenBase) entityHost).hasRigging()) {

                if(!entityHost.wantsToAttack(target, entityHost)){
                    return false;
                }

                boolean isArmed = entityHost.canUseCannonOrTorpedo() && ((EntityKansenBase) entityHost).canUseShell(((EntityKansenBase) entityHost).getActiveShellCategory());
                boolean isSailing = entityHost.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
                return isArmed && isSailing;
            }
        }
        return false;
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel p_212834_1_, @Nonnull AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        if(!p_212834_2_.getBrain().getMemory(ATTACK_TARGET).isPresent()){
            return false;
        }
        boolean withinrange = p_212834_2_.closerThan(p_212834_2_.getBrain().getMemory(ATTACK_TARGET).get(), p_212834_2_.getCannonRange()+2);
        return checkExtraStartConditions(p_212834_1_, p_212834_2_) && withinrange;
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        this.CannonAttackDelay = -1;
        this.torpedoAttackDelay = -1;
    }

    @Override
    protected void tick(ServerLevel p_212833_1_, AbstractEntityCompanion entity, long p_212833_3_) {
        if(entity instanceof EntityKansenBase) {
            entity.getBrain().setMemory(ATTACK_TARGET, getAttackTarget(entity));
            entity.lookAt(getAttackTarget(entity), 30, 30);
            if (--this.CannonAttackDelay == 0) {
                ((EntityKansenBase) entity).AttackUsingCannon(getAttackTarget(entity));
                this.CannonAttackDelay = 20+Mth.floor(MathUtil.getRand().nextFloat()*20);
            } else if (this.CannonAttackDelay < 0) {
                this.CannonAttackDelay = 80+Mth.floor(MathUtil.getRand().nextFloat()*20);
            }


            if (--this.torpedoAttackDelay == 0 && entity.isSailing()) {
                ((EntityKansenBase) entity).AttackUsingTorpedo(getAttackTarget(entity));
                this.torpedoAttackDelay = (int) (300 + (getRand().nextFloat() * 140));
            } else if (this.torpedoAttackDelay < 0) {
                this.torpedoAttackDelay = 80+Mth.floor(MathUtil.getRand().nextFloat()*20);
            }
        }

        if(entity.getBrain().getMemory(ATTACK_TARGET).map((target) ->target.isDeadOrDying() || target.removed).orElse(true)){
            this.doStop(p_212833_1_, entity, p_212833_3_);
        }
    }

    private void lookAtTarget(Mob p_233889_1_, LivingEntity p_233889_2_) {
        p_233889_1_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(p_233889_2_, true));
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }


}
