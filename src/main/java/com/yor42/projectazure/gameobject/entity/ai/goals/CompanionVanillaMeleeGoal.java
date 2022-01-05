package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.AbstractSwordUserBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

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
    public boolean shouldExecute() {
        Item item = this.entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem();
        boolean isEquippingsword = item instanceof SwordItem;

        if(this.entity.getAttackTarget() == null || !this.entity.getAttackTarget().isAlive()){
            return false;
        }

        if(this.entity instanceof AbstractSwordUserBase){
            if(((AbstractSwordUserBase)this.entity).shouldUseNonVanillaAttack()){
                return false;
            }
        }

        if(!isEquippingsword || this.entity.shouldUseGun()) {
            return false;
        }
        else if(this.entity instanceof EntityKansenBase) {
            boolean flag = !hasAttackableCannon(this.entity.getRigging()) && !((EntityKansenBase) this.entity).canUseShell(((EntityKansenBase) this.entity).getActiveShellCategory());
            if (flag) {
                this.target = this.entity.getAttackTarget();
                return true;
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {

        if(this.target == null || this.entity.getOwner() != null && (this.entity.getDistance(this.entity.getOwner())>=16 || this.target.getDistance(this.entity.getOwner())>=16)){
            return false;
        }


        return this.shouldExecute() || !this.entity.getNavigator().noPath();
    }


    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entity.setMeleeing(true);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {

        this.entity.setSprinting(this.entity.getNavigator().hasPath() && this.entity.getDistance(enemy)>4F);

        super.checkAndPerformAttack(enemy, distToEnemySqr);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.setMeleeing(false);
        this.entity.setSprinting(false);
    }
}
