package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.ISpellUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public abstract class AbstractCompanionMagicUser extends AbstractEntityCompanion implements ISpellUser {

    public AbstractCompanionMagicUser(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    public boolean shouldUseSpell(LivingEntity target){
        return this.getItemInHand(getSpellUsingHand()).isEmpty() && !isSwimming();
    }


}
