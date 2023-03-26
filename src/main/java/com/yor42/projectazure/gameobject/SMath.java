package com.yor42.projectazure.gameobject;

import net.minecraft.world.phys.Vec3;

// this one will help you when math method is gone :O
// you can remove this class when you have a better way to call the math
public class SMath
{
    public static double getHorizontalDistanceSqr(Vec3 pVector)
    {
        return pVector.x * pVector.x + pVector.z * pVector.z;
    }
}
