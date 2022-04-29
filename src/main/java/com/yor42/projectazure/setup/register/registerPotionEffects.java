package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

public class registerPotionEffects {

    public static MobEffect ACUTE_ORIPATHY = new AcuteOripathyEffect();
    public static RegistryObject<MobEffect> ACUTE_ORIPATHY_REGISTRY = Main.EFFECTS.register("acute_oripathy", ()->ACUTE_ORIPATHY);

    public static class AcuteOripathyEffect extends MobEffect{

        protected AcuteOripathyEffect() {
            super(MobEffectCategory.HARMFUL, 0x8b4235);
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

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MobEffect> event){};
}
