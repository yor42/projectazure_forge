package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.ISpellUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public abstract class AbstractCompanionMagicUser extends AbstractEntityCompanion implements ISpellUser {

    public AbstractCompanionMagicUser(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void StartShootingEntityUsingSpell(LivingEntity target) {
        this.setSpellDelay(this.getInitialSpellDelay());
        this.StartedSpellAttackTimeStamp = this.tickCount;
    }

    public boolean shouldUseSpell(){
        return this.getItemInHand(getSpellUsingHand()).isEmpty() && !isSwimming();
    };


}
