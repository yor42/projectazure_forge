package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasGunOrTorpedo;
import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionShipRangedAttackTask extends Task<AbstractEntityCompanion> {

    private int CannonAttackDelay;
    int torpedoAttackDelay;

    public CompanionShipRangedAttackTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion entityHost) {
        if(entityHost instanceof EntityKansenBase) {
            if (((EntityKansenBase) entityHost).hasRigging()) {
                boolean isArmed = hasGunOrTorpedo(entityHost.getRigging());
                boolean isSailing = entityHost.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
                return isArmed && isSailing;
            }
        }
        return false;
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerWorld p_212834_1_, @Nonnull AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return checkExtraStartConditions(p_212834_1_, p_212834_2_);
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        this.CannonAttackDelay = -1;
        this.torpedoAttackDelay = -1;
    }

    @Override
    protected void tick(ServerWorld p_212833_1_, AbstractEntityCompanion entity, long p_212833_3_) {
        if(entity instanceof EntityKansenBase) {
            lookAtTarget(entity, getAttackTarget(entity));
            if (--this.CannonAttackDelay == 0) {
                ((EntityKansenBase) entity).AttackUsingCannon(getAttackTarget(entity));
                this.CannonAttackDelay = 20+MathHelper.floor(MathUtil.getRand().nextFloat()*20);
            } else if (this.CannonAttackDelay < 0) {
                this.CannonAttackDelay = 80+MathHelper.floor(MathUtil.getRand().nextFloat()*20);
            }


            if (--this.torpedoAttackDelay == 0) {
                ((EntityKansenBase) entity).AttackUsingTorpedo(getAttackTarget(entity));
                this.torpedoAttackDelay = (int) (300 + (getRand().nextFloat() * 140));
            } else if (this.torpedoAttackDelay < 0) {
                this.torpedoAttackDelay = 80+MathHelper.floor(MathUtil.getRand().nextFloat()*20);
            }
        }
    }

    private void lookAtTarget(MobEntity p_233889_1_, LivingEntity p_233889_2_) {
        p_233889_1_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(p_233889_2_, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }


}
