package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public interface IAknOp {
    enums.OperatorClass getOperatorClass();
    SoundEvent getNormalAmbientSounds();
    SoundEvent getAffection1AmbientSounds();
    SoundEvent getAffection2AmbientSounds();
    SoundEvent getAffection3AmbientSounds();
}
