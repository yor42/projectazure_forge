package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.AbstractSwordUserBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasAttackableCannon;

public class CompanionVanillaMeleeGoal extends MeleeAttackGoal {
    private final AbstractEntityCompanion entity;
    @Nullable
    private LivingEntity target;

    public CompanionVanillaMeleeGoal(AbstractEntityCompanion creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
        this.entity = creature;
    }

    @Override
    public boolean canUse() {
        Item item = this.entity.getItemBySlot(EquipmentSlotType.MAINHAND).getItem();
        boolean isEquippingsword = item instanceof SwordItem;

        if(this.entity.getTarget() == null || !this.entity.getTarget().isAlive()){
            return false;
        }

        if(this.entity instanceof AbstractSwordUserBase){
            if(((AbstractSwordUserBase)this.entity).shouldUseNonVanillaAttack(this.entity.getTarget())){
                return false;
            }
        }

        if((!isEquippingsword && !this.entity.isAngry()) || this.entity.shouldUseGun()) {
            return false;
        }
        else if(this.entity instanceof EntityKansenBase) {
            boolean flag = !hasAttackableCannon(this.entity.getRigging()) && !((EntityKansenBase) this.entity).canUseShell(((EntityKansenBase) this.entity).getActiveShellCategory());
            if (!flag) {
                return false;
            }
        }
        this.target = this.entity.getTarget();
        return true;
    }

    public boolean canContinueToUse() {

        if(this.target == null || this.entity.getOwner() != null && (this.entity.distanceTo(this.entity.getOwner())>=16 || this.target.distanceTo(this.entity.getOwner())>=16)){
            return false;
        }


        return this.canUse() || !this.entity.getNavigation().isDone();
    }


    @Override
    public void start() {
        super.start();
        this.entity.setMeleeing(true);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {

        double d0 = this.getAttackReachSqr(enemy)+1;
        this.entity.setSprinting(this.entity.getNavigation().isInProgress() && this.entity.distanceTo(enemy)>4F);
        if(this.entity.isAngry() && this.entity.getOwner() == enemy && distToEnemySqr <= d0){
            this.resetAttackCooldown();
            this.mob.swing(Hand.MAIN_HAND);
            enemy.hurt(DamageSources.causeRevengeDamage(this.entity), this.entity.getAttackDamage());
        }
        else{
            super.checkAndPerformAttack(enemy, distToEnemySqr);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setMeleeing(false);
        this.entity.setSprinting(false);
    }
}
