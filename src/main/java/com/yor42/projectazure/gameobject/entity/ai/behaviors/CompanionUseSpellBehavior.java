package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.ISpellUser;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.*;

public class CompanionUseSpellBehavior extends DelayedBehaviour<AbstractEntityCompanion> {
    public CompanionUseSpellBehavior(AbstractEntityCompanion entity) {
        super(entity instanceof ISpellUser? ((ISpellUser) entity).getProjectilePreAnimationDelay():-1);
        cooldownFor((e)->10);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(LOOK_TARGET, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {

        if(this.delayTime<0 || entity.isAnimating()){
            return false;
        }

        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if(!(entity instanceof ISpellUser)){
            return false;
        }else if(target == null){
            return false;
        }
        else if(!entity.wantsToAttack(target, entity)){
            return false;
        }

        return ((ISpellUser) entity).shouldUseSpell(target);
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if (entity instanceof ISpellUser && target != null) {
            if (entity.closerThan(target, entity.getSpellRange())) {
                ((ISpellUser) entity).StartSpellAttack(target);
                entity.setAnimation(RegisterAI.Animations.USE_SPELL, ((ISpellUser) entity).getInitialSpellDelay());
            }
        }
    }

    @Override
    protected void tick(AbstractEntityCompanion entity) {
        super.tick(entity);
        entity.getNavigation().stop();
    }

    @Override
    protected void doDelayedAction(AbstractEntityCompanion entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if(target == null){
            return;
        }
        ((ISpellUser) entity).ShootProjectile(entity.level, target);
    }
}
