package com.yor42.projectazure.gameobject.misc;

import com.yor42.projectazure.setup.register.registerPotionEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;

public class ModFoods {

    public static FoodProperties ORIGINIUM_PRIME = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).alwaysEat().effect(()->(new MobEffectInstance(registerPotionEffects.ACUTE_ORIPATHY_REGISTRY.get(), 72000, 0)), 1).build();

}
