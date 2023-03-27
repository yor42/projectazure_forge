package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.Main;
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
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.registerSounds.SYRINGE_INJECT;

public class CompanionHealAllyAndPlayerTask extends Behavior<AbstractEntityCompanion> {

    private int rangedAttackTime = -1;
    private int ItemSwapDelay = 0;
    private final int maxRangedAttackTime, attackIntervalMin;
    private final float attackRadius;
    private InteractionHand PotionHand;
    @Nullable
    private InteractionHand SwappedHand;
    private final float maxAttackDistance;


    public CompanionHealAllyAndPlayerTask(int maxRangedAttackTime, int attackIntervalMin, float attackRadius) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, RegisterAI.HEAL_TARGET.get(), MemoryStatus.VALUE_PRESENT, RegisterAI.HEAL_POTION_INDEX.get(), MemoryStatus.REGISTERED, RegisterAI.REGENERATION_POTION_INDEX.get(), MemoryStatus.REGISTERED));
        this.maxRangedAttackTime = maxRangedAttackTime;
        this.attackIntervalMin = attackIntervalMin;
        this.attackRadius = attackRadius;
        this.maxAttackDistance = attackRadius*attackRadius;
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel p_212832_1_, @Nonnull AbstractEntityCompanion entity) {
        if(this.ItemSwapDelay>0){
            this.ItemSwapDelay--;
            return false;
        }

        if(entity.getOwner() != null && !entity.isSleeping() && !entity.isOrderedToSit()) {


            return entity.getBrain().getMemory(RegisterAI.HEAL_TARGET.get()).map((target)->{
                int inventoryoindex;
                this.getPotioninHand(entity).ifPresent((hand)->this.PotionHand = hand);
                if(this.PotionHand!=null){
                    return true;
                }
                else {
                    inventoryoindex = this.getPotionFromInv(entity, target);
                    if (inventoryoindex <= -1) {
                        return false;
                    }
                    else if (entity.getFreeHand().isPresent()) {
                        this.ChangeItem(entity, entity.getFreeHand().get(), inventoryoindex, false);
                    }
                }
                return false;
            }).orElse(false);
        }
        return false;
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        return entity.getBrain().getMemory(RegisterAI.HEAL_TARGET.get()).isPresent();
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        p_212831_2_.getBrain().getMemory(RegisterAI.HEAL_TARGET.get()).ifPresent((target)-> {
            p_212831_2_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        });
    }

    @Override
    protected void tick(@Nonnull ServerLevel p_212833_1_, @Nonnull AbstractEntityCompanion entity, long p_212833_3_) {
        if(this.PotionHand != null) {
            entity.getBrain().getMemory(RegisterAI.HEAL_TARGET.get()).ifPresent((target)->{
                ItemStack PotionStack = entity.getItemInHand(this.PotionHand);
                Item PotionItem = PotionStack.getItem();
                float CloseEnoughDistance = PotionItem instanceof ThrowablePotionItem? this.maxAttackDistance:2;
                if(entity.distanceTo(target)>=CloseEnoughDistance){
                    BehaviorUtils.setWalkAndLookTargetMemories(entity, target, 1F, (int)CloseEnoughDistance);
                }

                else if(--this.rangedAttackTime <= 0) {
                    if (PotionItem instanceof ThrowablePotionItem) {
                        double d0 = entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
                        float f = (float)Math.sqrt(d0) / this.attackRadius;
                        this.throwPotion(entity, target, entity.getItemInHand(this.PotionHand));
                        entity.getItemInHand(this.PotionHand).shrink(1);
                        this.rangedAttackTime = Mth.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                    }
                    else{
                        List<MobEffectInstance> list = PotionUtils.getMobEffects(PotionStack);
                        for(MobEffectInstance effect : list){
                            if(effect.getEffect().isInstantenous()){
                                effect.getEffect().applyInstantenousEffect(entity, entity.getOwner(), target, effect.getAmplifier(), 1);
                            }
                            else {
                                target.addEffect(effect);
                            }
                        }

                        SoundEvent soundevent;
                        if(entity.getItemInHand(this.PotionHand).getItem() instanceof ItemSyringe){
                            soundevent = SYRINGE_INJECT;
                        }
                        else{
                            soundevent = SoundEvents.GENERIC_DRINK;
                        }

                        target.playSound(soundevent, 0.8F+(0.4F* MathUtil.getRand().nextFloat()), 0.8F+(0.4F* MathUtil.getRand().nextFloat()));
                        Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(()->target), new PlaySoundPacket(soundevent, SoundSource.PLAYERS, target.getX(),target.getY(),target.getZ(), 0.8F+(0.4F*MathUtil.rand.nextFloat()), 0.8F+(0.4F*MathUtil.rand.nextFloat())));
                        entity.swing(this.PotionHand);
                        entity.getItemInHand(this.PotionHand).shrink(1);
                        this.rangedAttackTime = 20;
                    }
                }
            });
        }
    }

    @Override
    protected void stop(@Nonnull ServerLevel p_212835_1_, @Nonnull AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        if(this.SwappedHand != null){
            this.ChangeItem(p_212835_2_, this.SwappedHand, p_212835_2_.getItemSwapIndex(this.SwappedHand),  true);
        }
        this.PotionHand=null;
        p_212835_2_.getBrain().eraseMemory(RegisterAI.HEAL_TARGET.get());
        this.rangedAttackTime = -1;
    }

    public void throwPotion(AbstractEntityCompanion entity, LivingEntity target, ItemStack potionStack) {
        Potion potion = PotionUtils.getPotion(potionStack);
        Vec3 vector3d = target.getDeltaMovement();
        double d0 = target.getX() + vector3d.x - entity.getX();
        double d1 = target.getEyeY() - (double)1.1F - entity.getY();
        double d2 = target.getZ() + vector3d.z - entity.getZ();
        float f = (float)Math.sqrt(d0 * d0 + d2 * d2);

        ThrownPotion potionentity = new ThrownPotion(entity.getCommandSenderWorld(), entity);
        potionentity.setItem(PotionUtils.setPotion(potionStack, potion));
        potionentity.setXRot(potionentity.getXRot() - 20.0F);
        potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITCH_THROW, entity.getSoundSource(), 1.0F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
        entity.swing(this.PotionHand, true);
        entity.level.addFreshEntity(potionentity);
    }

    private int getPotionFromInv(AbstractEntityCompanion entity, LivingEntity HealTarget){
        MemoryModuleType<Integer> preferredEffect = HealTarget.getHealth()<6? RegisterAI.HEAL_POTION_INDEX.get(): RegisterAI.REGENERATION_POTION_INDEX.get();
        MemoryModuleType<Integer> otherEffect = preferredEffect == RegisterAI.HEAL_POTION_INDEX.get()? RegisterAI.REGENERATION_POTION_INDEX.get(): RegisterAI.HEAL_POTION_INDEX.get();
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        return brain.getMemory(preferredEffect).orElse(brain.getMemory(otherEffect).orElse(-1));
    }

    private Optional<InteractionHand> getPotioninHand(AbstractEntityCompanion entity) {
        for(InteractionHand hand : InteractionHand.values()) {
            ItemStack potion = entity.getItemInHand(hand);
            if (PotionUtils.getPotion(potion) == Potions.EMPTY) {
                continue;
            }
            List<MobEffectInstance> Effects = PotionUtils.getMobEffects(potion);
            if (!Effects.isEmpty()) {
                for (MobEffectInstance effect : Effects) {
                    if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                        return Optional.empty();
                    } else if (effect.getEffect() == MobEffects.HEAL || effect.getEffect() == MobEffects.REGENERATION) {
                        return Optional.of(hand);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private void ChangeItem(AbstractEntityCompanion entity, InteractionHand hand, int index, boolean isResetting){
        ItemStack Buffer = entity.getItemInHand(hand);
        entity.setItemInHand(hand, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        this.ItemSwapDelay = 10;
        entity.setItemswapIndex(hand, isResetting? -1 : index);
        this.SwappedHand = isResetting? null : hand;
    }

}
