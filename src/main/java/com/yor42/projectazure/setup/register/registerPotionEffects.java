package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class registerPotionEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Constants.MODID);

    public static Effect ACUTE_ORIPATHY = new AcuteOripathyEffect();
    public static RegistryObject<Effect> ACUTE_ORIPATHY_REGISTRY = EFFECTS.register("acute_oripathy", ()->ACUTE_ORIPATHY);

    public static class AcuteOripathyEffect extends Effect{

        protected AcuteOripathyEffect() {
            super(EffectType.HARMFUL, 0x8b4235);
        }

        @Override
        public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
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
