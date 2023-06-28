package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

import static com.tac.guns.client.handler.ShootingHandler.calcShootTickGap;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;
import static net.minecraft.world.entity.ai.memory.MemoryModuleType.LOOK_TARGET;

public class CompanionUseGunBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {

    private int attackTime = -1;
    private int seeTime;

    public CompanionUseGunBehavior(){
        cooldownFor((e)->50);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if(target == null || !entity.wantsToAttack(target, entity)){
            return false;
        }
        ItemStack stack = entity.getMainHandItem();
        if(stack.getItem() instanceof GunItem){
            boolean canusegun = entity.shouldUseGun();
            boolean hastarget = !target.isDeadOrDying();
            boolean isnotSitting = !entity.isOrderedToSit();
            boolean entitycanAttack = !entity.isSleeping() && isnotSitting;
            return canusegun && entitycanAttack && hastarget && entity.closerThan(target, entity.getGunRange());
        }

        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AbstractEntityCompanion entity, long gameTime) {
        return checkExtraStartConditions(level, entity);
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {

        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if(target == null || !entity.wantsToAttack(target, entity)){
            return;
        }

        if(entity.closerThan(target, entity.getSpellRange()) && entity.getSensing().hasLineOfSight(target)){
            this.clearWalkTarget(entity);
        }
        else{
            BehaviorUtils.setWalkAndLookTargetMemories(entity, target, 1,0);
        }
    }

    @Override
    protected void tick(AbstractEntityCompanion entity) {

        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if(target == null){
            return;
        }

        ItemStack gunStack = entity.getGunStack();
        Item Item = gunStack.getItem();
        if(Item instanceof GunItem gunItem) {
            boolean canSee = entity.getSensing().hasLineOfSight(target);
            boolean flag1 = this.seeTime > 0;

            if (canSee != flag1) {
                this.seeTime = 0;
            }

            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!entity.isUsingGun() && this.seeTime >= 5 && gunStack != ItemStack.EMPTY && getRemainingAmmo(gunStack) > 0) {
                BrainUtils.setMemory(entity, RegisterAI.ANIMATION.get(), RegisterAI.Animations.SHOOT_GUN);
            }
            boolean flag = entity.isUsingGun();
            entity.getBrain().setMemory(LOOK_TARGET, new EntityTracker(target, true));
            if (flag) {
                if (!canSee && this.seeTime < -80) {
                    BrainUtils.clearMemory(entity, RegisterAI.ANIMATION.get());
                } else if (canSee) {
                    BrainUtils.setMemory(entity, RegisterAI.ANIMATION.get(), RegisterAI.Animations.SHOOT_GUN);
                    boolean hasAmmo = gunStack.getOrCreateTag().getInt("AmmoCount") > 0;
                    boolean reloadable = entity.HasRightMagazine(gunStack);
                    Gun modifiedGun = gunItem.getModifiedGun(gunStack);
                    float rpm = (float)modifiedGun.getGeneral().getRate();
                    if (hasAmmo && --this.attackTime <= 0) {
                        entity.AttackUsingGun(gunStack);
                        this.attackTime = (int) calcShootTickGap((int)rpm);
                    } else if (gunStack.getItem() instanceof GunItem && getRemainingAmmo(gunStack) <= 0 && !entity.isReloadingMainHand() && reloadable) {
                        entity.setReloadDelay(gunStack);
                    }
                }
            } else if (this.seeTime >= -80) {
                BrainUtils.setMemory(entity, RegisterAI.ANIMATION.get(), RegisterAI.Animations.SHOOT_GUN);
            }
        }

        super.tick(entity);
    }

    @Override
    protected void stop(AbstractEntityCompanion entity) {
        BrainUtils.clearMemory(entity, RegisterAI.ANIMATION.get());
        this.seeTime = 0;
        this.attackTime = -1;
        super.stop(entity);
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Pair.of(RegisterAI.ANIMATION.get(), MemoryStatus.VALUE_ABSENT));
    }
}
