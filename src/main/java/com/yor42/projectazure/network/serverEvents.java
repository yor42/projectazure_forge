package com.yor42.projectazure.network;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    public static void SpawnItemParticles(Entity entity, ItemStack itemstack)
    {
        ServerLevel serverlevel = (ServerLevel)entity.getLevel();

        for (int i = 0; i < 16; ++i)
        {
            Vec3 vector3d = new Vec3(((double) serverlevel.getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vector3d = vector3d.xRot(-entity.getXRot() * ((float) Math.PI / 180F));
            vector3d = vector3d.yRot(-entity.getYRot() * ((float) Math.PI / 180F));
            double d0 = (double) (-serverlevel.getRandom().nextFloat()) * 0.6D - 0.3D;
            Vec3 vector3d1 = new Vec3(((double) serverlevel.getRandom().nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vector3d1 = vector3d1.xRot(-entity.getXRot() * ((float) Math.PI / 180F));
            vector3d1 = vector3d1.yRot(-entity.getYRot() * ((float) Math.PI / 180F));
            vector3d1 = vector3d1.add(entity.getX(), entity.getEyeY(), entity.getZ());

            serverlevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemstack), vector3d1.x, vector3d1.y, vector3d1.z, 1, vector3d.x, vector3d.y + 0.05D, vector3d.z, 1.0D);
        }
    }

    public static void spawnCannonParticleModerate(ServerLevel world, double X, double Y, double Z, double NormalLookX, double NormalLookZ){
        for (int i = 0; i < 24; i++)
        {
            double ran1 = world.getRandom().nextFloat() - 0.5F;
            double ran2 = world.getRandom().nextFloat();
            double ran3 = world.getRandom().nextFloat();
            final double x = X + NormalLookX - 0.5D + 0.05D * i;
            final double FinalZ = Z + NormalLookZ - 0.5D + 0.05D * i;
            world.sendParticles(ParticleTypes.LARGE_SMOKE, x, Y+0.6D+ran1, FinalZ, 1, NormalLookX*0.3D*ran2, 0.05D*ran2, NormalLookZ*0.3D*ran2, 1.0D);
            world.sendParticles(ParticleTypes.LARGE_SMOKE, x, Y+1.0D+ran1, FinalZ, 1, NormalLookX*0.3D*ran3, 0.05D*ran3, NormalLookZ*0.3D*ran3, 1.0D);
        }
    }

    public static void spawnPatHeartParticle(Entity target){
        ServerLevel world = (ServerLevel)target.getLevel();
        double d0 = world.getRandom().nextGaussian() * 0.02D;
        double d1 = world.getRandom().nextGaussian() * 0.02D;
        double d2 = world.getRandom().nextGaussian() * 0.02D;
        world.sendParticles(ParticleTypes.HEART, target.getRandomX(1.0D), target.getRandomY() + 0.5D, target.getRandomZ(1.0D), 1, d0, d1, d2, 1.0D);
    }

    public static void spawnPatSmokeParticle(Entity target){
        ServerLevel world = (ServerLevel)target.getLevel();
        double d0 = world.getRandom().nextGaussian() * 0.02D;
        double d1 = world.getRandom().nextGaussian() * 0.02D;
        double d2 = world.getRandom().nextGaussian() * 0.02D;
        world.sendParticles(ParticleTypes.SMOKE, target.getRandomX(1.0D), target.getRandomY() + 0.5D, target.getRandomZ(1.0D), 1, d0, d1, d2, 1.0D);
    }

    public static void spawnTeleportParticle(Entity target){
        ServerLevel world = (ServerLevel)target.getLevel();

        for(int i = 0; i < 32; ++i)
        {
            world.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY() + world.getRandom().nextDouble() * 2.0D, target.getZ(), 1, world.getRandom().nextGaussian(), 0.0D, world.getRandom().nextGaussian(), 1.0D);
        }
    }

    public static void spawnLimitBreakParticle(Entity target){
        ServerLevel world = (ServerLevel)target.getLevel();
        for(int i = 0; i < 5; ++i) {
            double d0 = world.getRandom().nextGaussian() * 0.02D;
            double d1 = world.getRandom().nextGaussian() * 0.02D;
            double d2 = world.getRandom().nextGaussian() * 0.02D;
            world.sendParticles(ParticleTypes.HAPPY_VILLAGER, target.getRandomX(1.0D), target.getRandomY() + 1.0D, target.getRandomZ(1.0D), 1, d0, d1, d2, 1.0D);
        }
    }
}
