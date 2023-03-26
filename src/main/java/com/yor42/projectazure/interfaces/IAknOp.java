package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.libs.enums;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;

public interface IAknOp {
    @Nullable
    default Component getRealname(){
        return null;
    }
    enums.OperatorClass getOperatorClass();
    SoundEvent getNormalAmbientSounds();
    SoundEvent getAffection1AmbientSounds();
    SoundEvent getAffection2AmbientSounds();
    SoundEvent getAffection3AmbientSounds();
}
