package com.yor42.projectazure.gameobject.entity.companion.sworduser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TieredItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;

public abstract class AbstractSwordUserBase extends AbstractEntityCompanion {

    protected int StartedAttackTimeStamp = -1;
    protected int AttackCount = 0;
    protected static final DataParameter<Integer> ATTACKDELAY = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);

    protected AbstractSwordUserBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(ATTACKDELAY, -1);
    }

    public int getAttackDelay(){
        return this.getDataManager().get(ATTACKDELAY);
    }

    public void setSpellDelay(int value) {
        this.getDataManager().set(ATTACKDELAY, value);
    }

    /*
    Delay between Each Attack
     */
    public abstract int getInitialAttackDelay();

    /*
    Delay of Pre animation before Actual Damage Deals

    FAQ
    Q1. Why arraylist?
    A1. Because some entity apparently deals damage multiple times each attack.
     */
    public abstract ArrayList<Integer> getAttackPreAnimationDelay();

    public abstract ArrayList<Item> getTalentedWeaponList();

    public boolean hasMeleeItem(){
        Item item = this.getHeldItemMainhand().getItem();
        return this.getTalentedWeaponList().contains(item) || item instanceof SwordItem;
    }

    public abstract float getAttackRange(boolean isUsingTalentedWeapon);

    public boolean shouldUseNonVanillaAttack(){
        return this.hasMeleeItem() && !this.isSwimming() && !this.isSitting() && this.getRidingEntity() == null;
    }

    public boolean isAttacking(){
        return this.getAttackDelay()>0;
    }

    public boolean isUsingTalentedWeapon(){
        return this.getTalentedWeaponList().contains(this.getHeldItemMainhand().getItem());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.getDataManager().set(ATTACKDELAY, compound.getInt("attackdelay"));
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if(!this.getEntityWorld().isRemote()) {
            int currentspelldelay = this.getAttackDelay();
            @Nullable
            LivingEntity target = this.getAttackTarget();
            if (currentspelldelay > 0) {
                this.getNavigator().clearPath();
                if (target == null || !target.isAlive()) {
                    this.setSpellDelay(0);
                    this.AttackCount = 0;
                } else {
                    this.AttackCount+=1;
                    setSpellDelay(currentspelldelay - 1);
                    if (this.getAttackPreAnimationDelay().contains(this.ticksExisted - this.StartedAttackTimeStamp) && this.getDistance(target)<=this.getAttackRange(this.isUsingTalentedWeapon())) {
                        this.PerformMeleeAttack(target, this.getAttackDamage(), this.AttackCount);
                    }
                }
            }
            else if(this.AttackCount>0){
                this.AttackCount = 0;
            }
        }
    }

    public abstract void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount);

    public void writeAdditional(@Nonnull CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putInt("attackdelay", this.getDataManager().get(ATTACKDELAY));
    }

    public void StartAttackingEntity() {
        this.setSpellDelay((int) (this.getInitialAttackDelay() *this.getAttackSpeedModifier(this.isUsingTalentedWeapon())));
        this.StartedAttackTimeStamp = this.ticksExisted;
    }

    public abstract float getAttackSpeedModifier(boolean isUsingTalentedWeapon);

}
