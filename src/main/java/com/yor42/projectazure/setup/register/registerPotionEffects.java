package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class registerPotionEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Constants.MODID);
    public static RegistryObject<Effect> ACUTE_ORIPATHY_REGISTRY = EFFECTS.register("acute_oripathy", AcuteOripathyEffect::new);

    public static RegistryObject<Effect> FROSTBITE_REGISTRY = EFFECTS.register("frostbite", ()-> new AcuteOripathyEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, UUID.randomUUID().toString(), -0.11F, AttributeModifier.Operation.MULTIPLY_TOTAL));

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

    public static class FrostbiteEffect extends Effect{

        protected FrostbiteEffect() {
            super(EffectType.HARMFUL, 0xa5f2f3);
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
