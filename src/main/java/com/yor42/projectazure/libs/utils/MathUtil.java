package com.yor42.projectazure.libs.utils;

public class MathUtil {
    //Oh yeah its Big brain time
    public static float DegreeToRadian(float degree){
        return (float) (degree*(Math.PI/180));
    }

    public static float RadianRoDegree(Float Radian){
        return (float) (Radian*(180/Math.PI));
    }

    public static float LimitAngleMovement(float value, float maxDegree, float minDegree, boolean isValueDegree, boolean ShouldReturnRadian){
        float Value = value;
        float Result;
        if(!isValueDegree){
            Value = RadianRoDegree(value);
        }
        if(Value>maxDegree)
            Result = maxDegree;
        else Result = Math.max(Value, minDegree);

        if(ShouldReturnRadian){
            return DegreeToRadian(Result);
        }
        else
            return Result;
    }

}
