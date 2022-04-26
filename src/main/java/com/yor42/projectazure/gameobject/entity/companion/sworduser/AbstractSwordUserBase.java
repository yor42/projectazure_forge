package com.yor42.projectazure.gameobject.entity.companion.sworduser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractSwordUserBase extends AbstractEntityCompanion implements IMeleeAttacker {

    protected AbstractSwordUserBase(EntityType<? extends TameableEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public boolean hasMeleeItem(){
        Item item = this.getItemInHand(this.getNonVanillaMeleeAttackHand()).getItem();
        return this.getTalentedWeaponList().contains(item) || item instanceof SwordItem;
    }

    public boolean shouldUseNonVanillaAttack(LivingEntity target){
        return this.hasMeleeItem() && !this.isSwimming() && !this.isOrderedToSit() && this.getVehicle() == null;
    }

    public boolean isTalentedWeaponinMainHand(){
        return this.isTalentedWeapon(this.getMainHandItem());
    }

    public void StartMeleeAttackingEntity() {
        this.setMeleeAttackDelay((int) (this.getInitialMeleeAttackDelay() *this.getAttackSpeedModifier(this.isTalentedWeaponinMainHand())));
        this.StartedMeleeAttackTimeStamp = this.tickCount;
    }

    @Override
    public ItemStack getMainHandItem() {
        return super.getMainHandItem();
    }

    @Override
    public ItemStack getOffhandItem() {
        return super.getOffhandItem();
    }
}
