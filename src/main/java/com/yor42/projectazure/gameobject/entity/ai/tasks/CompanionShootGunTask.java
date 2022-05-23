package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.*;

public class CompanionShootGunTask extends Task<AbstractEntityCompanion> {

    private int attackTime = -1;
    private int seeTime;

    public CompanionShootGunTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld p_212832_1_, @Nonnull AbstractEntityCompanion entity) {
        return this.getValidGunHand(entity).map((hand)->{
            ItemStack gunStack = entity.getItemInHand(hand);
            Item gunItem = gunStack.getItem();
            if(gunItem instanceof ItemGunBase){
                return entity.getBrain().getMemory(ATTACK_TARGET).map((target)->{
                    boolean canusegun = entity.shouldUseGun();
                    boolean hastarget = !target.isDeadOrDying();
                    boolean entitycanAttack = !entity.isSleeping() && !entity.isOrderedToSit();
                    return canusegun && entitycanAttack && hastarget;
                }).orElse(false);
            }
            return false;
        }).orElse( false);
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerWorld p_212834_1_, @Nonnull AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
    }

    @Override
    protected void start(@Nonnull ServerWorld p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        p_212831_2_.getBrain().eraseMemory(WALK_TARGET);
    }

    @Override
    protected void tick(@Nonnull ServerWorld p_212833_1_, AbstractEntityCompanion entity, long p_212833_3_) {
        entity.getBrain().getMemory(ATTACK_TARGET).ifPresent((target) ->
                this.getValidGunHand(entity).ifPresent((hand) -> {
                    ItemStack gunStack = entity.getItemInHand(hand);
                    Item gunItem = gunStack.getItem();
                    boolean canSee = entity.getSensing().canSee(target);
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
                        entity.setUsingGun(true);
                    }
                    boolean flag = entity.isUsingGun();
                    entity.getBrain().setMemory(LOOK_TARGET, new EntityPosWrapper(target, true));
                    if (flag) {
                        if (!canSee && this.seeTime < -80) {
                            entity.stopUsingItem();
                            entity.setUsingGun(false);
                        } else if (canSee) {
                            entity.setUsingGun(true);
                            boolean hasAmmo = getRemainingAmmo(gunStack) > 0;
                            boolean reloadable = entity.HasRightMagazine(((ItemGunBase) gunItem).getAmmoType());
                            if (hasAmmo && --this.attackTime <= 0) {
                                entity.AttackUsingGun(target, gunStack, hand);
                                this.attackTime = ((ItemGunBase) gunStack.getItem()).getMinFireDelay();
                            } else if (gunStack.getItem() instanceof ItemGunBase && getRemainingAmmo(gunStack) <= 0 && !entity.isReloadingMainHand() && reloadable) {
                                entity.setReloadDelay();
                            }
                        }
                    } else if (this.seeTime >= -80) {
                        entity.setUsingGun(true);
                    }
                }));
    }

    @Override
    protected void stop(@Nonnull ServerWorld p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        p_212835_2_.setUsingGun(false);
    }

    private Optional<Hand> getValidGunHand(AbstractEntityCompanion companion){
        for(Hand hand:Hand.values()){
            if(companion.getItemInHand(hand).getItem() instanceof ItemGunBase){
                return Optional.of(hand);
            }
        }
        return Optional.empty();
    }
}
