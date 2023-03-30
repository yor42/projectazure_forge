package com.spacecat.summer.objects.math;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

// if you see this just feel free to use it
public class PatMath
{
    public static boolean viewTarget(Entity entity, int i)
	{
		// north
        if (((entity.getYRot() > 134.0F && entity.getYRot() < 226.0F) || (entity.getYRot() > -226.0F && entity.getYRot() < -134.0F)))
        {
            return i == 1;
        }
    	// east
        else if ((entity.getYRot() > 224.0F && entity.getYRot() < 316.0F) || (entity.getYRot() > -136.0F && entity.getYRot() < -44.0F))
        {
            return i == 2;
        }
		// west
        else if ((entity.getYRot() > 44.0F && entity.getYRot() < 136.0F) || (entity.getYRot() > -316.0F && entity.getYRot() < -224.0F))
        {
            return i == 3;
        }
    	// south
        else
        {
            return i == 4;
        }
	}

	public static boolean targetsView00(Entity player, Entity entity)
	{
		// player:north target:south
        // player:east target:west
        // player:south target:north
        // player:west target:east
		return (viewTarget(player, 1) && viewTarget(entity, 4)) || (viewTarget(player, 2) && viewTarget(entity, 3)) || (viewTarget(player, 4) && viewTarget(entity, 1)) || (viewTarget(player, 3) && viewTarget(entity, 2));
	}

	public static boolean targetsView01(Entity player, Entity entity)
	{
		// player:north target:west
        // player:east target:north
        // player:south target:east
        // player:west target:south
		return (viewTarget(player, 1) && viewTarget(entity, 3)) || (viewTarget(player, 2) && viewTarget(entity, 1)) || (viewTarget(player, 4) && viewTarget(entity, 2)) || (viewTarget(player, 3) && viewTarget(entity, 4));
	}

	public static boolean targetsView02(Entity player, Entity entity)
	{
		// player:north target:east
        // player:east target:south
        // player:south target:west
        // player:west target:north
		return (viewTarget(player, 1) && viewTarget(entity, 2)) || (viewTarget(player, 2) && viewTarget(entity, 4)) || (viewTarget(player, 4) && viewTarget(entity, 3)) || (viewTarget(player, 3) && viewTarget(entity, 1));
	}

    public static Vec3 calculateView(float x, float y)
    {
        float f = x * ((float)Math.PI / 180.0F);
        float f1 = -y * ((float)Math.PI / 180.0F);
        float f2 = (float)Math.cos(f1);
        float f3 = (float)Math.sin(f1);
        float f4 = (float)Math.cos(f);
        float f5 = (float)Math.sin(f);
        return new Vec3(f3 * f4, -f5, f2 * f4);
    }

    public static boolean targetsView(Entity player, AABB target_aabb)
    {
        byte step = 0; // you can sniper with this
        boolean result = false;
        Vec3 view_vec3 = calculateView(player.getXRot(), player.getYRot());
        Vec3 start_vec3 = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        view_vec3 = view_vec3.scale(0.5D); // reduce for boost detailed
        Vec3 end_vec3 = start_vec3.add(view_vec3);

        while (!result && step < 4)
        {
            end_vec3 = end_vec3.add(view_vec3);
            result = target_aabb.contains(end_vec3);
            ++step;
        }

        return result;
    }
}
