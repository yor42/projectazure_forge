package com.yor42.projectazure.network;

import com.yor42.projectazure.libs.utils.ClientUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientEvents {

    @OnlyIn(Dist.CLIENT)
    public static void spawnCannonParticleModerate(double X, double Y, double Z, double NormalLookX, double NormalLookZ){
        Level world = ClientUtils.getClientWorld();
        for (int i = 0; i < 24; i++)
        {
            double ran1 = MathUtil.getRand().nextFloat() - 0.5F;
            double ran2 = MathUtil.getRand().nextFloat();
            double ran3 = MathUtil.getRand().nextFloat();
            final double x = X + NormalLookX - 0.5D + 0.05D * i;
            final double FinalZ = Z + NormalLookZ - 0.5D + 0.05D * i;
            world.addParticle(ParticleTypes.LARGE_SMOKE, x, Y+0.6D+ran1, FinalZ, NormalLookX*0.3D*ran2, 0.05D*ran2, NormalLookZ*0.3D*ran2);
            world.addParticle(ParticleTypes.LARGE_SMOKE, x, Y+1.0D+ran1, FinalZ, NormalLookX*0.3D*ran3, 0.05D*ran3, NormalLookZ*0.3D*ran3);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnPatHeartParticle(int EntityID){
        Level world = ClientUtils.getClientWorld();
        Entity target = world.getEntity(EntityID);
        if(target != null) {
            double d0 = MathUtil.rand.nextGaussian() * 0.02D;
            double d1 = MathUtil.rand.nextGaussian() * 0.02D;
            double d2 = MathUtil.rand.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.HEART, target.getRandomX(1.0D), target.getRandomY() + 0.5D, target.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnPatSmokeParticle(int EntityID){
        Level world = ClientUtils.getClientWorld();
        Entity target = world.getEntity(EntityID);
        if(target != null) {
            double d0 = MathUtil.rand.nextGaussian() * 0.02D;
            double d1 = MathUtil.rand.nextGaussian() * 0.02D;
            double d2 = MathUtil.rand.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.SMOKE, target.getRandomX(1.0D), target.getRandomY() + 0.5D, target.getRandomZ(1.0D), d0, d1, d2);

        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnTeleportParticle(int EntityID){
        Level world = ClientUtils.getClientWorld();
        Entity target = world.getEntity(EntityID);
        if(target != null) {
            for(int i = 0; i < 32; ++i) {
                world.addParticle(ParticleTypes.PORTAL, target.getX(), target.getY() + MathUtil.getRand().nextDouble() * 2.0D, target.getZ(), MathUtil.getRand().nextGaussian(), 0.0D, MathUtil.getRand().nextGaussian());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnLimitBreakParticle(int EntityID){
        Level world = ClientUtils.getClientWorld();
        Entity target = world.getEntity(EntityID);
        if(target != null) {
            for(int i = 0; i < 5; ++i) {
                double d0 = MathUtil.getRand().nextGaussian() * 0.02D;
                double d1 = MathUtil.getRand().nextGaussian() * 0.02D;
                double d2 = MathUtil.getRand().nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, target.getRandomX(1.0D), target.getRandomY() + 1.0D, target.getRandomZ(1.0D), d0, d1, d2);
            }
        }
    }

    public static void SpawnItemParticles(int EntityID, ItemStack stack) {
        Level world = ClientUtils.getClientWorld();
        Entity target = world.getEntity(EntityID);
        if(target != null) {
            for (int i = 0; i < 16; ++i) {
                Vec3 vector3d = new Vec3(((double) MathUtil.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                vector3d = vector3d.xRot(-target.xRot * ((float) Math.PI / 180F));
                vector3d = vector3d.yRot(-target.yRot * ((float) Math.PI / 180F));
                double d0 = (double) (-MathUtil.rand.nextFloat()) * 0.6D - 0.3D;
                Vec3 vector3d1 = new Vec3(((double) MathUtil.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
                vector3d1 = vector3d1.xRot(-target.xRot * ((float) Math.PI / 180F));
                vector3d1 = vector3d1.yRot(-target.yRot * ((float) Math.PI / 180F));
                vector3d1 = vector3d1.add(target.getX(), target.getEyeY(), target.getZ());
                world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vector3d1.x, vector3d1.y, vector3d1.z, vector3d.x, vector3d.y + 0.05D, vector3d.z);
            }
        }

    }

    public static void PlaySound(SoundEvent sound, double x, double y, double z, SoundSource category, float pitch, float volume){
        ClientLevel world = Minecraft.getInstance().level;
        if(world == null){
            return;
        }
        world.playSound(Minecraft.getInstance().player, x, y, z, sound, category, pitch, volume);
    }

}
