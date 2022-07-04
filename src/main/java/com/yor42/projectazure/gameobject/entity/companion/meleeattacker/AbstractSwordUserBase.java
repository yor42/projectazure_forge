package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class AbstractSwordUserBase extends AbstractEntityCompanion implements IMeleeAttacker {

    protected AbstractSwordUserBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public boolean hasMeleeItem(){
        Item item = this.getItemInHand(this.getNonVanillaMeleeAttackHand()).getItem();
        return this.getTalentedWeaponList().contains(item) || (item instanceof SwordItem && allowVanillaSwords());
    }

    private boolean allowVanillaSwords(){
        return true;
    }

    public boolean shouldUseNonVanillaAttack(LivingEntity target){
        boolean flag = this.hasMeleeItem() && !this.isSwimming() && !this.isOrderedToSit() && this.getVehicle() == null;
        return flag;
    }

    public boolean isTalentedWeaponinMainHand(){
        return this.isTalentedWeapon(this.getMainHandItem());
    }

    public void StartMeleeAttackingEntity() {
        this.setMeleeAttackDelay((int) (this.MeleeAttackAnimationLength() *this.getAttackSpeedModifier(this.isTalentedWeaponinMainHand())));
        this.StartedMeleeAttackTimeStamp = this.tickCount;
    }

    @Nonnull
    @Override
    public ItemStack getMainHandItem() {
        return super.getMainHandItem();
    }

    @Nonnull
    @Override
    public ItemStack getOffhandItem() {
        return super.getOffhandItem();
    }
}
