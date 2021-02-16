package com.yor42.projectazure.network.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    private Entity SharedMob = null;

    public static World getClientWorld()
    {
        return Minecraft.getInstance().world;
    }

    public void setSharedMob(Entity mob){
        this.SharedMob = mob;
    }

    public Entity getSharedMob(){
        return this.SharedMob;
    }
}
