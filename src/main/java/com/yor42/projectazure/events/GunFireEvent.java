package com.yor42.projectazure.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public class GunFireEvent extends PlayerEvent {

    /*
    Shameless Copy of MrCrayfish's gun mod API
    @author Ocelot
     */

    private final ItemStack gun;

    public GunFireEvent(Player player, ItemStack gun) {
        super(player);
        this.gun = gun;
    }

    public ItemStack getGun() {
        return this.gun;
    }

    public boolean isRemote(){
        return this.getPlayer().getCommandSenderWorld().isClientSide();
    }

    @Cancelable
    public static class PreFire extends GunFireEvent
    {
        public PreFire(Player player, ItemStack stack)
        {
            super(player, stack);
        }
    }

    @Cancelable
    public static class PostFire extends GunFireEvent
    {
        public PostFire(Player player, ItemStack stack)
        {
            super(player, stack);
        }
    }
}
