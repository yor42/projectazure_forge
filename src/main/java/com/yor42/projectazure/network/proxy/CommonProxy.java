package com.yor42.projectazure.network.proxy;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.defined;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = defined.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {
    ItemStack Sharedstack;
    public void setSharedMob(AbstractEntityCompanion mob){
    }

    public AbstractEntityCompanion getSharedMob(){
        return null;
    }

    public void setSharedStack(ItemStack stack){
        this.Sharedstack = stack;
    }
    public ItemStack getSharedStack() {
        return this.Sharedstack;
    }

}
