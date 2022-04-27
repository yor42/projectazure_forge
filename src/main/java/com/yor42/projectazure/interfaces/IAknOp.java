package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.libs.enums;
import net.minecraft.sounds.SoundEvent;

public interface IAknOp {
    enums.OperatorClass getOperatorClass();
    SoundEvent getNormalAmbientSounds();
    SoundEvent getAffection1AmbientSounds();
    SoundEvent getAffection2AmbientSounds();
    SoundEvent getAffection3AmbientSounds();
}
