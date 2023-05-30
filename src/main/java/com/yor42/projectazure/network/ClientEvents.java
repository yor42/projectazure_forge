package com.yor42.projectazure.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class ClientEvents {
    public static void PlaySound(SoundEvent sound, double x, double y, double z, SoundSource category, float pitch, float volume){
        ClientLevel world = Minecraft.getInstance().level;
        if(world == null){
            return;
        }
        world.playSound(Minecraft.getInstance().player, x, y, z, sound, category, pitch, volume);
    }

}
