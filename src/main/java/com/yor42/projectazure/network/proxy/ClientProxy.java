package com.yor42.projectazure.network.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    private Entity SharedMob = null;
    private ItemStack SharedStack = ItemStack.EMPTY;

    public static World getClientWorld()
    {
        return Minecraft.getInstance().world;
    }

    //client player
    public static PlayerEntity getClientPlayer()
    {
        return Minecraft.getInstance().player;
    }

    public void setSharedMob(Entity mob){
        this.SharedMob = mob;
    }

    public Entity getSharedMob(){
        return this.SharedMob;
    }

    public void setSharedStack(ItemStack stack){this.SharedStack = stack;}
    public ItemStack getSharedStack() {
        return this.SharedStack;
    }
}
