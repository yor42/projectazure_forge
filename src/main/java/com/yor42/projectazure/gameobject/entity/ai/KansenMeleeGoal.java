package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

public class KansenMeleeGoal extends MeleeAttackGoal {
    private EntityKansenBase entity;

    public KansenMeleeGoal(EntityKansenBase creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
        this.entity = creature;
    }

    @Override
    public boolean shouldExecute() {
        //TODO: check if they can attack with gun first

        Item item = this.entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem();

        boolean isEquippingsword = item instanceof SwordItem;

        if(isEquippingsword) {
            return super.shouldExecute();
        }
        else{
            return false;
        }
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
