package com.yor42.projectazure.gameobject.misc;

import com.yor42.projectazure.setup.register.registerPotionEffects;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;

public class ModFoods {

    public static Food ORIGINIUM_PRIME = (new Food.Builder()).nutrition(2).saturationMod(0.3F).alwaysEat().effect(()->(new EffectInstance(registerPotionEffects.ACUTE_ORIPATHY, 72000, 0)), 1).build();

}
