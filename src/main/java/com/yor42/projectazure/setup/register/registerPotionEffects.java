package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.RegistryObject;

public class registerPotionEffects {

    public static MobEffect ACUTE_ORIPATHY = new AcuteOripathyEffect();
    public static RegistryObject<Effect> ACUTE_ORIPATHY_REGISTRY = registerManager.EFFECTS.register("acute_oripathy", ()->ACUTE_ORIPATHY);

    public static class AcuteOripathyEffect extends MobEffect{

        protected AcuteOripathyEffect() {
            super(EffectType.HARMFUL, 0x8b4235);
        }

        @Override
        public void applyEffectTick(Entity entityLivingBaseIn, int amplifier) {
            super.applyEffectTick(entityLivingBaseIn, amplifier);
            entityLivingBaseIn.hurt(DamageSources.ACUTE_ORIPATHY, 10F+(float) amplifier/2);
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
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
