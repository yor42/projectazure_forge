package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.world.World;

public class ProjectileUtils {

    public static void spawnClientCannonShot(int AmmoCategory, double x, double y, double z, double accelX, double accelY, double accelZ){
        World world = ClientProxy.getClientWorld();

        enums.AmmoCategory ammotype = enums.AmmoCategory.values()[AmmoCategory];

        //get dummy property for client. its not real anyway,
        AmmoProperties properties = new AmmoProperties(ammotype, 0,0,0,0,0,false,false);
        EntityCannonPelllet pellet = new EntityCannonPelllet(world, x,y,z,accelX,accelY,accelZ, properties);
    }

}
