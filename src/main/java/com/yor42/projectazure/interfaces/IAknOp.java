package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.libs.enums;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface IAknOp {
    @Nullable
    default ITextComponent getRealname(){
        return null;
    }
    enums.OperatorClass getOperatorClass();
    SoundEvent getNormalAmbientSounds();
    SoundEvent getAffection1AmbientSounds();
    SoundEvent getAffection2AmbientSounds();
    SoundEvent getAffection3AmbientSounds();
}
