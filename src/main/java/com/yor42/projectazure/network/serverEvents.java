package com.yor42.projectazure.network;

import com.yor42.projectazure.gameobject.containers.riggingcontainer.IRiggingContainerSupplier;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerEntity;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class serverEvents {

    public static void spawnStarter(ServerPlayerEntity player, int StarterID){
        World world = player.world;
        EntityType<?> entitytype;

        if(!world.isRemote)
        {
            switch (StarterID){
                case defined.STARTER_SAKURA: {
                    entitytype = registerManager.AYANAMI.get();
                    break;
                }
                default:{
                    entitytype = null;
                    break;
                }
            }
            if (entitytype == null){
                player.sendMessage(new TranslationTextComponent("message.invalidstarter"), player.getUniqueID());
            }
            else{
                EntityKansenBase entity = (EntityKansenBase) entitytype.spawn((ServerWorld)world, player.getActiveItemStack(), player, player.getPosition(), SpawnReason.SPAWN_EGG, false, false);
                if (entity != null){
                    if(!player.isCreative()) {
                        if(player.getHeldItem(Hand.MAIN_HAND).getItem() == registerItems.Rainbow_Wisdom_Cube.get())
                            player.getHeldItem(Hand.MAIN_HAND).shrink(1);
                        else
                            player.getHeldItem(Hand.OFF_HAND).shrink(1);
                    }
                    entity.setTamedBy(player);
                    entity.setAffection(40.0F);
                }
                player.closeScreen();
            }
        }
    }

}