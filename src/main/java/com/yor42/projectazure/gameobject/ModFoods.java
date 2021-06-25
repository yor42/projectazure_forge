package com.yor42.projectazure.gameobject;

import com.yor42.projectazure.setup.register.RegisterPotionEffects;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {

    public static Food ORIGINIUM_PRIME = (new Food.Builder()).hunger(2).saturation(0.3F).setAlwaysEdible().effect(()->(new EffectInstance(RegisterPotionEffects.ACUTE_ORIPATHY, 72000, 0)), 1).build();

}
