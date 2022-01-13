package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SoundUtils {

    public static void playReloadSoundOnEntity(World world, Entity entity, SoundEvent sound, float volume, float pitch, boolean repeat, boolean moving){
        if(!world.isClientSide()){

        }
        else{
            ClientProxy client = ClientProxy.getClientProxy();
            client.PlaySoundonEntity(entity, sound, volume, pitch, repeat, true, false);
        }
    }

}
