package com.spacecat.summer.objects.math;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

// feel free to use it
public class RayMath
{
    public static boolean isFront(Entity player, Entity target)
    {
        float target_yaw_radians = (float)Math.toRadians(target.getYHeadRot());
        float target_pitch_radians = (float)Math.toRadians(target.getXRot());
        Vec3 target_forward = new Vec3(-Math.sin(target_yaw_radians) * Math.cos(target_pitch_radians), -Math.sin(target_pitch_radians), Math.cos(target_yaw_radians) * Math.cos(target_pitch_radians)).normalize();

        float player_yaw_radians = (float)Math.toRadians(player.getYHeadRot());
        float player_pitch_radians = (float)Math.toRadians(player.getXRot());
        Vec3 player_forward = new Vec3(-Math.sin(player_yaw_radians) * Math.cos(player_pitch_radians), -Math.sin(player_pitch_radians), Math.cos(player_yaw_radians) * Math.cos(player_pitch_radians)).normalize();


        Vec3 target_position = target.position();
        Vec3 player_position = player.position();

        Vec3 target2player_position_vec3 = player_position.subtract(target_position).normalize();
        Vec3 player2target_position_vec3 = target_position.subtract(player_position).normalize();


        double fb_target_dot_vec3 = target_forward.dot(target2player_position_vec3);
        double fb_player_dot_vec3 = player_forward.dot(player2target_position_vec3);

        // double lr_target_dot_vec3 = target_forward.cross(new Vec3(0.0D, 1.0D, 0.0D)).dot(target2player_position_vec3);
        // double lr_player_dot_vec3 = player_forward.cross(new Vec3(0.0D, 1.0D, 0.0D)).dot(player2target_position_vec3);

        return fb_target_dot_vec3 > 0.0D && fb_player_dot_vec3 > 0.0D;
        // return fb_target_dot_vec3 < 0.0D && fb_player_dot_vec3 > 0.0D; back
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

    public static boolean targetsView(Entity player, AABB target_aabb, Vec3 view_vec3)
    {
        byte step = 0; // you can sniper with this
        boolean result = false;
        Vec3 start_vec3 = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        Vec3 temp_view_vec3 = view_vec3.scale(0.5D); // reduce for boost detailed
        Vec3 end_vec3 = start_vec3.add(temp_view_vec3);

        while (!result && step < 4)
        {
            end_vec3 = end_vec3.add(temp_view_vec3);
            result = target_aabb.contains(end_vec3);
            ++step;
        }

        return result;
    }
}
