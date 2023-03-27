package com.yor42.projectazure.network;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.yor42.projectazure.libs.Constants.StarterList;

public class serverEvents {
    public static void spawnStarter(ServerPlayer player, int StarterID){
        Level world = player.level;
        EntityType<?> entitytype;

        if(!world.isClientSide)
        {
            entitytype = StarterList[StarterID];
            if (entitytype == null){
                player.sendMessage(new TranslatableComponent("message.invalidstarter"), player.getUUID());
            }
            else{
                EntityKansenBase entity = (EntityKansenBase) entitytype.spawn((ServerLevel)world, player.getUseItem(), player, player.blockPosition(), MobSpawnType.SPAWN_EGG, false, false);
                if (entity != null){
                    if(!player.isCreative()) {
                        if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == RegisterItems.Rainbow_Wisdom_Cube.get())
                            player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                        else
                            player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);
                    }
                    entity.tame(player);
                    entity.setAffection(40.0F);
                    entity.MaxFillHunger();
                    entity.setMorale(150);
                }
            }
        }
    }

    public static void selectStartkit(ServerPlayer playerEntity, byte id, InteractionHand hand) {
        playerEntity.getItemInHand(hand).shrink(1);
        if(id == 0){
            //AKN
            playerEntity.setItemInHand(hand, new ItemStack(RegisterItems.SPAWN_AMIYA.get()));
        }
        else{
            ItemStack cubeStack = new ItemStack(RegisterItems.Rainbow_Wisdom_Cube.get());
            CompoundTag nbt = cubeStack.getOrCreateTag();
            nbt.putUUID("owner", playerEntity.getUUID());
            playerEntity.setItemInHand(hand, cubeStack);
        }
    }
}
