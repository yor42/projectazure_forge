package com.yor42.projectazure.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public class GunFireEvent extends PlayerEvent {

    /*
    Shameless Copy of MrCrayfish's gun mod API
    @author Ocelot
     */

    private final ItemStack gun;

    public GunFireEvent(PlayerEntity player, ItemStack gun) {
        super(player);
        this.gun = gun;
    }

    public ItemStack getGun() {
        return this.gun;
    }

    public boolean isRemote(){
        return this.getPlayer().getEntityWorld().isRemote();
    }

    @Cancelable
    public static class PreFire extends GunFireEvent
    {
        public PreFire(PlayerEntity player, ItemStack stack)
        {
            super(player, stack);
        }
    }

    @Cancelable
    public static class PostFire extends GunFireEvent
    {
        public PostFire(PlayerEntity player, ItemStack stack)
        {
            super(player, stack);
        }
    }
}
