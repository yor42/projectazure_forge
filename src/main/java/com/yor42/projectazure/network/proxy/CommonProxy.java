package com.yor42.projectazure.network.proxy;

import com.yor42.projectazure.libs.defined;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = defined.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    public void setSharedMob(Entity mob){
    }

    public Entity getSharedMob(){
        return null;
    }
}
