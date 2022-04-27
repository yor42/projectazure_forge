package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public abstract class AbstractCompanionMagicUser extends AbstractEntityCompanion implements ISpellUser {

    public AbstractCompanionMagicUser(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    public void StartShootingEntityUsingSpell(LivingEntity target) {
        this.setSpellDelay(this.getInitialSpellDelay());
        this.StartedSpellAttackTimeStamp = this.tickCount;
    }

    @Override
    public ItemStack getGunStack() {
        return super.getGunStack();
    }

    @Override
    public ItemStack getItemInHand(InteractionHand p_184586_1_) {
        return super.getItemInHand(p_184586_1_);
    }

    public boolean shouldUseSpell(){
        if(getGunStack().getItem() instanceof ItemGunBase) {
            boolean hasAmmo = getRemainingAmmo(getGunStack()) > 0;
            boolean reloadable = HasRightMagazine((((ItemGunBase) getGunStack().getItem()).getAmmoType()));

            return !(hasAmmo || reloadable);
        }
        else return getItemInHand(getSpellUsingHand()).isEmpty() && !isSwimming();
    };


}
