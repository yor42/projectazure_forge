package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.ai.behaviors.base.ExtendedItemSwitchingBehavior;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.tools.ItemSyringe;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.PlaySoundPacket;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.registerSounds.SYRINGE_INJECT;

public class CompanionHealPlayerAndAllyBehavior extends ExtendedItemSwitchingBehavior<AbstractEntityCompanion> {

    List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = List.of(Pair.of(RegisterAI.HEAL_POTION_INDEX.get(), MemoryStatus.REGISTERED), Pair.of(RegisterAI.REGENERATION_POTION_INDEX.get(), MemoryStatus.REGISTERED)
    , Pair.of(RegisterAI.HEAL_TARGET.get(), MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));

    @Nullable
    private InteractionHand hand;

    public CompanionHealPlayerAndAllyBehavior(){
        cooldownFor((e)->10);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        if(entity.getOwner() == null || entity.isSleeping() || entity.isOrderedToSit()){
            return false;
        }

        LivingEntity healTarget = BrainUtils.getMemory(entity, RegisterAI.HEAL_TARGET.get());

        if(healTarget == null){
            return false;
        }

        InteractionHand hand = this.getItemHand(entity, (stack)-> {
            if (PotionUtils.getPotion(stack) == Potions.EMPTY) {
                return false;
            }
            List<MobEffectInstance> Effects = PotionUtils.getMobEffects(stack);

            if (Effects.isEmpty()) {
                return false;
            }

            for (MobEffectInstance effect : Effects) {
                if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    return false;
                } else if (effect.getEffect() == MobEffects.HEAL || effect.getEffect() == MobEffects.REGENERATION) {
                    return true;
                }
            }
            return false;
        });

        if(hand == null){

            Integer healidx = BrainUtils.getMemory(entity, RegisterAI.HEAL_POTION_INDEX.get());
            Integer regenidx = BrainUtils.getMemory(entity, RegisterAI.REGENERATION_POTION_INDEX.get());

            int index;

            index = Objects.requireNonNullElseGet(healidx, () -> regenidx == null ? -1 : regenidx);

            if(index>-1){
                this.ChangeItem(index, entity);
            }

            return false;
        }

        this.hand = hand;
        return true;
    }

    @Override
    protected boolean shouldKeepRunning(AbstractEntityCompanion entity) {

        LivingEntity healTarget = BrainUtils.getMemory(entity, RegisterAI.HEAL_TARGET.get());
        InteractionHand hand = this.getItemHand(entity, (stack)-> {
            if (PotionUtils.getPotion(stack) == Potions.EMPTY) {
                return false;
            }
            List<MobEffectInstance> Effects = PotionUtils.getMobEffects(stack);

            if (Effects.isEmpty()) {
                return false;
            }

            for (MobEffectInstance effect : Effects) {
                if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    return false;
                } else if (effect.getEffect() == MobEffects.HEAL || effect.getEffect() == MobEffects.REGENERATION) {
                    return true;
                }
            }
            return false;
        });
        return healTarget != null && hand != null && !healTarget.hasEffect(MobEffects.REGENERATION) && healTarget.getHealth()<10;
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {

        LivingEntity target = BrainUtils.getMemory(entity, RegisterAI.HEAL_TARGET.get());
        if(target == null || this.hand == null){
            return;
        }

        ItemStack PotionStack = entity.getItemInHand(this.hand);
        Item PotionItem = PotionStack.getItem();

        float CloseEnoughDistance = PotionItem instanceof ThrowablePotionItem? 4:1;

        if(entity.distanceTo(target)>CloseEnoughDistance){
            BehaviorUtils.setWalkAndLookTargetMemories(entity, target, 1F, (int)CloseEnoughDistance);
        }
        else {
            if (PotionItem instanceof ThrowablePotionItem) {
                throwPotion(entity, target, this.hand);
                entity.getItemInHand(this.hand).shrink(1);
                return;
            }
            List<MobEffectInstance> list = PotionUtils.getMobEffects(PotionStack);
            for (MobEffectInstance effect : list) {
                if (effect.getEffect().isInstantenous()) {
                    effect.getEffect().applyInstantenousEffect(entity, entity.getOwner(), target, effect.getAmplifier(), 1);
                } else {
                    target.addEffect(effect);
                }
            }

            SoundEvent soundevent;
            if (entity.getItemInHand(this.hand).getItem() instanceof ItemSyringe) {
                soundevent = SYRINGE_INJECT;
            } else {
                soundevent = SoundEvents.GENERIC_DRINK;
            }

            target.playSound(soundevent, 0.8F + (0.4F * MathUtil.getRand().nextFloat()), 0.8F + (0.4F * MathUtil.getRand().nextFloat()));
            Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), new PlaySoundPacket(soundevent, SoundSource.PLAYERS, target.getX(), target.getY(), target.getZ(), 0.8F + (0.4F * MathUtil.rand.nextFloat()), 0.8F + (0.4F * MathUtil.rand.nextFloat())));
            entity.swing(this.hand);
            entity.getItemInHand(this.hand).shrink(1);
        }
    }

    private static void throwPotion(AbstractEntityCompanion entity, LivingEntity target, InteractionHand hand) {
        ItemStack potionStack = entity.getItemInHand(hand);
        Potion potion = PotionUtils.getPotion(potionStack);
        Vec3 vector3d = target.getDeltaMovement();
        double d0 = target.getX() + vector3d.x - entity.getX();
        double d1 = target.getEyeY() - (double)1.1F - entity.getY();
        double d2 = target.getZ() + vector3d.z - entity.getZ();
        float f = (float)Math.sqrt(d0 * d0 + d2 * d2);

        ThrownPotion potionentity = new ThrownPotion(entity.getLevel(), entity);
        potionentity.setItem(PotionUtils.setPotion(potionStack, potion));
        potionentity.setXRot(potionentity.getXRot() - 20.0F);
        potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITCH_THROW, entity.getSoundSource(), 1.0F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
        entity.swing(hand, true);
        entity.level.addFreshEntity(potionentity);
    }

    @Override
    protected InteractionHand SwapHand(AbstractEntityCompanion entity) {
        return entity.getFreeHand().orElse(InteractionHand.OFF_HAND);
    }
}
