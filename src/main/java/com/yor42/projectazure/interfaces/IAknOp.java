package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.libs.enums;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public interface IAknOp {
    enums.OperatorClass getOperatorClass();
    SoundEvent getNormalAmbientSounds();
    SoundEvent getAffection1AmbientSounds();
    SoundEvent getAffection2AmbientSounds();
    SoundEvent getAffection3AmbientSounds();
}