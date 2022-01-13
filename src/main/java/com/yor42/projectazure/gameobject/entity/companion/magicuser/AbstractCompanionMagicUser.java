package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public abstract class AbstractCompanionMagicUser extends AbstractEntityCompanion implements ISpellUser {

    public AbstractCompanionMagicUser(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public boolean shouldUseSpell(){
        if(this.getGunStack().getItem() instanceof ItemGunBase) {
            boolean hasAmmo = getRemainingAmmo(this.getGunStack()) > 0;
            boolean reloadable = this.HasRightMagazine((((ItemGunBase) this.getGunStack().getItem()).getAmmoType()));

            return !(hasAmmo || reloadable);
        }
        else return this.getItemInHand(getSpellUsingHand()).isEmpty() && !this.isSwimming();
    }

    public void StartShootingEntityUsingSpell(LivingEntity target) {
        this.setSpellDelay(this.getInitialSpellDelay());
        this.StartedSpellAttackTimeStamp = this.tickCount;
    }
}
