package com.yor42.projectazure.interfaces;

import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;

public interface IChargeFire {

    int getMaxChargeTime();

    @Nullable
    default SoundEvent getChargeSound(){
        return null;
    }

}
