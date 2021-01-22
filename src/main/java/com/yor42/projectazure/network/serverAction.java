package com.yor42.projectazure.network;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class serverAction {

    public static void spawnStarter(ServerPlayerEntity player, int StarterID){
        World world = player.world;
        EntityType<?> entitytype;
        EntityKansenBase entity;

        if(!world.isRemote)
        {
            switch (StarterID){
                case defined.STARTER_SAKURA: {
                    entitytype = registerEntity.AYANAMI.get();
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
                if(entitytype.spawn((ServerWorld)world, player.getActiveItemStack(), player, player.getPosition(), SpawnReason.SPAWN_EGG, false, false) == null){
                    if(!player.isCreative()) {
                        player.getActiveItemStack().shrink(1);
                    }
                }
                else {
                    player.sendMessage(new TranslationTextComponent("message.starterspawnfailed"), player.getUniqueID());
                }
                player.closeScreen();
            }
        }
    }

}
