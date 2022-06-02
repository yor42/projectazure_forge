package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public abstract class AbstractCompanionMagicUser extends AbstractEntityCompanion implements ISpellUser {

    public AbstractCompanionMagicUser(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void StartShootingEntityUsingSpell(LivingEntity target) {
        this.setSpellDelay(this.getInitialSpellDelay());
        this.StartedSpellAttackTimeStamp = this.tickCount;
    }

    @Override
    public ItemStack getItemInHand(Hand p_184586_1_) {
        return super.getItemInHand(p_184586_1_);
    }

    public boolean shouldUseSpell(){

        return getItemInHand(getSpellUsingHand()).isEmpty() && !isSwimming();
    };


}
