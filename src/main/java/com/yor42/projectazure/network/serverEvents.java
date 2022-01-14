package com.yor42.projectazure.network;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import static com.yor42.projectazure.libs.Constants.StarterList;

public class serverEvents {
    public static void spawnStarter(ServerPlayerEntity player, int StarterID){
        World world = player.level;
        EntityType<?> entitytype;

        if(!world.isClientSide)
        {
            entitytype = StarterList[StarterID];
            if (entitytype == null){
                player.sendMessage(new TranslationTextComponent("message.invalidstarter"), player.getUUID());
            }
            else{
                EntityKansenBase entity = (EntityKansenBase) entitytype.spawn((ServerWorld)world, player.getUseItem(), player, player.blockPosition(), SpawnReason.SPAWN_EGG, false, false);
                if (entity != null){
                    if(!player.isCreative()) {
                        if(player.getItemInHand(Hand.MAIN_HAND).getItem() == registerItems.Rainbow_Wisdom_Cube.get())
                            player.getItemInHand(Hand.MAIN_HAND).shrink(1);
                        else
                            player.getItemInHand(Hand.OFF_HAND).shrink(1);
                    }
                    entity.tame(player);
                    entity.setAffection(40.0F);
                    entity.MaxFillHunger();
                    entity.setMorale(150);
                }
            }
        }
    }

}
