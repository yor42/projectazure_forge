package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Objects;

public class CompanionShipRangedAttackBehavior extends DelayedBehaviour<AbstractEntityCompanion> {
    public CompanionShipRangedAttackBehavior(AbstractEntityCompanion entity) {
        super(entity instanceof EntityKansenBase? ((EntityKansenBase) entity).getAnimationDelayBeforeFire():-1);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if(target == null || !entity.wantsToAttack(target, entity) || this.delayTime<0 || !(entity instanceof EntityKansenBase) || !(((EntityKansenBase) entity).hasRigging())){
            return false;
        }
        EntityKansenBase host = (EntityKansenBase) entity;
        boolean isArmed = host.canUseShell(host.getActiveShellCategory()) && (host.can_UseCannon() || host.can_UseTorpedo() && host.isSailing());
        boolean isSailing = host.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
        boolean withinrange = host.closerThan(target, host.getCannonRange());
        return isArmed && isSailing && withinrange;
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {

        if(entity instanceof EntityKansenBase) {
            BehaviorUtils.lookAtEntity(entity, Objects.requireNonNull(BrainUtils.getTargetOfEntity(entity)));
            BrainUtils.setForgettableMemory(entity, RegisterAI.ANIMATION.get(), RegisterAI.Animations.SHOOT_CANNON, entity.CannonAttackAnimLength());
        }
        super.start(entity);
    }

    @Override
    protected void doDelayedAction(AbstractEntityCompanion entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if (target == null) {
            return;
        }

        if(entity instanceof EntityKansenBase host) {
            if (!host.AttackUsingCannon(target)){
                host.AttackUsingTorpedo(target);
            }
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Pair.of(RegisterAI.ANIMATION.get(), MemoryStatus.VALUE_ABSENT));
    }
}
