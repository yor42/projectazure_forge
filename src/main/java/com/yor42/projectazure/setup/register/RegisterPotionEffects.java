package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.DamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public class RegisterPotionEffects {

    public static Effect ACUTE_ORIPATHY = new AcuteOripathyEffect();
    public static RegistryObject<Effect> ACUTE_ORIPATHY_REGISTRY = registerManager.EFFECTS.register("acute_oripathy", ()->ACUTE_ORIPATHY);

    public static class AcuteOripathyEffect extends Effect{

        protected AcuteOripathyEffect() {
            super(EffectType.HARMFUL, 0x8b4235);
        }

        @Override
        public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
            super.performEffect(entityLivingBaseIn, amplifier);
            entityLivingBaseIn.attackEntityFrom(DamageSources.ACUTE_ORIPATHY, 10F+(float) amplifier/2);
        }

        @Override
        public boolean isReady(int duration, int amplifier) {
            int j = 25 >> amplifier;
            if (j > 0) {
                return duration % j == 0;
            } else {
                return true;
            }
        }
    }

    public static void register(){}


}
