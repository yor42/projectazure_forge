package com.yor42.projectazure.network.proxy;

import net.minecraft.entity.Entity;

public class ClientProxy extends CommonProxy {

    private Entity SharedMob = null;

    public void setSharedMob(Entity mob){
        this.SharedMob = mob;
    }

    public Entity getSharedMob(){
        return this.SharedMob;
    }
}
