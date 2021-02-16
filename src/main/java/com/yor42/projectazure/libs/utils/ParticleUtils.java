package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleUtils {

    @OnlyIn(Dist.CLIENT)
    public static void spawnCannonParticleModerate(double X, double Y, double Z, double NormalLookX, double NormalLookZ){
        World world = ClientProxy.getClientWorld();
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


}
