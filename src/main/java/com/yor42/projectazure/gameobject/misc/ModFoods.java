package com.yor42.projectazure.gameobject.misc;

import com.yor42.projectazure.setup.register.registerPotionEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static FoodProperties ORIGINIUM_PRIME = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).alwaysEat().effect(()->(new MobEffectInstance(registerPotionEffects.ACUTE_ORIPATHY, 72000, 0)), 1).build();

}
