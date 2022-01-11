package com.yor42.projectazure.gameobject.entity.companion.sworduser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;

public abstract class AbstractSwordUserBase extends AbstractEntityCompanion implements IMeleeAttacker {

    protected AbstractSwordUserBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public boolean hasMeleeItem(){
        Item item = this.getHeldItemMainhand().getItem();
        return this.getTalentedWeaponList().contains(item) || item instanceof SwordItem;
    }
    public boolean shouldUseNonVanillaAttack(LivingEntity target){
        return this.hasMeleeItem() && !this.isSwimming() && !this.isSitting() && this.getRidingEntity() == null;
    }

    public boolean isUsingTalentedWeapon(){
        return this.getTalentedWeaponList().contains(this.getHeldItemMainhand().getItem());
    }

    public void StartMeleeAttackingEntity() {
        this.setMeleeAttackDelay((int) (this.getInitialMeleeAttackDelay() *this.getAttackSpeedModifier(this.isUsingTalentedWeapon())));
        this.StartedMeleeAttackTimeStamp = this.ticksExisted;
    }

    public abstract float getAttackSpeedModifier(boolean isUsingTalentedWeapon);

}
