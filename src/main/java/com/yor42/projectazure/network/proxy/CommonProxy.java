package com.yor42.projectazure.network.proxy;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {
    public boolean keyFirePressedMainhand;
    public boolean keyFirePressedOffhand;

    public void setSharedMob(AbstractEntityCompanion mob){
    }

    public AbstractEntityCompanion getSharedMob(){
        return null;
    }

    public PlayerEntity getPlayerClient() {
        return null;
    }


    public void PlaySoundonEntity(Entity entity, SoundEvent sound, float volume, float pitch, boolean repeat, boolean moving, boolean gunpos) {

    }
}
