package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class registerPotionEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Constants.MODID);
    public static RegistryObject<MobEffect> ACUTE_ORIPATHY_REGISTRY = EFFECTS.register("acute_oripathy", AcuteOripathyEffect::new);

    public static RegistryObject<MobEffect> FROSTBITE_REGISTRY = EFFECTS.register("frostbite", ()-> new FrostbiteEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, UUID.randomUUID().toString(), -0.11F, AttributeModifier.Operation.MULTIPLY_TOTAL));

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
        public List<ItemStack> getCurativeItems() {
            return new ArrayList<>();
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

    public static class FrostbiteEffect extends MobEffect{

        protected FrostbiteEffect() {
            super(MobEffectCategory.HARMFUL, 0xa5f2f3);
        }

        @Override
        public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
            super.applyEffectTick(entityLivingBaseIn, amplifier);
            entityLivingBaseIn.hurt(DamageSources.FROSTBITE, 2+(float) amplifier/2);
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
