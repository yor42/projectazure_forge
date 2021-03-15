package com.yor42.projectazure.network.proxy;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    private AbstractEntityCompanion SharedMob = null;
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

    public void setSharedMob(AbstractEntityCompanion mob){
        this.SharedMob = mob;
    }

    public AbstractEntityCompanion getSharedMob(){
        return this.SharedMob;
    }

    public void setSharedStack(ItemStack stack){this.SharedStack = stack;}
    public ItemStack getSharedStack() {
        return this.SharedStack;
    }

    public static ClientProxy getClientProxy(){
        return (ClientProxy) Main.PROXY;
    }

    @Override
    public PlayerEntity getPlayerClient() {
        return Minecraft.getInstance().player;
    }

    @Override
    public void PlaySoundonEntity(Entity entity, SoundEvent sound, float volume, float pitch, boolean repeat, boolean moving, boolean gunpos) {

    }
}
