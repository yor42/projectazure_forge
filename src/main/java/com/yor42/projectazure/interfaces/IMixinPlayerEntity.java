package com.yor42.projectazure.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;

import java.util.Optional;

public interface IMixinPlayerEntity {

    CompoundNBT getEntityonBack();

    boolean setEntityonBack(CompoundNBT compound);

    void forcesetEntityonBack(CompoundNBT compound);

    Optional<Entity> removeEntityOnBack();

    Optional<Entity> forceremoveEntityOnBack();

    Optional<Entity> respawnEntityOnBack(CompoundNBT compound);


}
