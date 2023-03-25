package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public abstract class AbstractSwordUserBase extends AbstractEntityCompanion implements IMeleeAttacker {

    protected AbstractSwordUserBase(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    public boolean hasMeleeItem(){
        Item item = this.getItemInHand(this.getNonVanillaMeleeAttackHand()).getItem();
        return this.getTalentedWeaponList().contains(item) || (item instanceof SwordItem && allowVanillaSwords());
    }

    private boolean allowVanillaSwords(){
        return true;
    }

    public boolean isTalentedWeaponinMainHand(){
        return this.isTalentedWeapon(this.getMainHandItem());
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
