package com.yor42.projectazure.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public interface IMixinPlayerEntity {

    CompoundTag getEntityonBack();

    boolean setEntityonBack(CompoundTag compound);

    void forcesetEntityonBack(CompoundTag compound);

    Optional<Entity> removeEntityOnBack();

    Optional<Entity> forceremoveEntityOnBack();

    Optional<Entity> respawnEntityOnBack(CompoundTag compound);


}
