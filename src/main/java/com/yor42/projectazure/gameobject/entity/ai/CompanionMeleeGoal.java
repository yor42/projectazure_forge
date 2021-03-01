package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.canUseCannon;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasAttackableCannon;

public class CompanionMeleeGoal extends MeleeAttackGoal {
    private AbstractEntityCompanion entity;

    public CompanionMeleeGoal(EntityKansenBase creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
        this.entity = creature;
    }

    @Override
    public boolean shouldExecute() {
        //TODO: check if they can attack with gun first

        Item item = this.entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem();

        boolean isEquippingsword = item instanceof SwordItem;

        if(!isEquippingsword) {
            return false;
        }
        else{
            if(this.entity instanceof EntityKansenBase) {
                boolean flag = !hasAttackableCannon(((EntityKansenBase) this.entity).getRigging()) && !((EntityKansenBase) this.entity).canUseAmmo(((EntityKansenBase) this.entity).getActiveAmmoCategory());
                return flag && super.shouldExecute() && isEquippingsword;
            }
        }
        return false;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entity.setMeleeing(true);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.setMeleeing(false);
    }
}
