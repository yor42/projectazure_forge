package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.tools.ItemSyringe;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.PlaySoundPacket;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.potion.*;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.registerSounds.SYRINGE_INJECT;
import static net.minecraft.potion.Effects.HEAL;
import static net.minecraft.potion.Effects.REGENERATION;
import static net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY_AND_SELF;

public class CompanionHealAllyAndPlayerTask extends Task<AbstractEntityCompanion> {

    private int rangedAttackTime = -1;
    private int ItemSwapDelay = 0;
    private final int maxRangedAttackTime, attackIntervalMin;
    private final float attackRadius;
    private Hand PotionHand;
    @Nullable
    private Hand SwappedHand;
    private final float maxAttackDistance;


    public CompanionHealAllyAndPlayerTask(int maxRangedAttackTime, int attackIntervalMin, float attackRadius) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, RegisterAI.HEAL_TARGET.get(), MemoryModuleStatus.VALUE_PRESENT, RegisterAI.HEAL_POTION_INDEX.get(), MemoryModuleStatus.REGISTERED, RegisterAI.REGENERATION_POTION_INDEX.get(), MemoryModuleStatus.REGISTERED));
        this.maxRangedAttackTime = maxRangedAttackTime;
        this.attackIntervalMin = attackIntervalMin;
        this.attackRadius = attackRadius;
        this.maxAttackDistance = attackRadius*attackRadius;
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld p_212832_1_, @Nonnull AbstractEntityCompanion entity) {
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
    protected boolean canStillUse(@Nonnull ServerWorld p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        return entity.getBrain().getMemory(RegisterAI.HEAL_TARGET.get()).isPresent();
    }

    @Override
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        p_212831_2_.getBrain().getMemory(RegisterAI.HEAL_TARGET.get()).ifPresent((target)-> {
            p_212831_2_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(target, true));
        });
    }

    @Override
    protected void tick(@Nonnull ServerWorld p_212833_1_, @Nonnull AbstractEntityCompanion entity, long p_212833_3_) {
        if(this.PotionHand != null) {
            entity.getBrain().getMemory(RegisterAI.HEAL_TARGET.get()).ifPresent((target)->{
                ItemStack PotionStack = entity.getItemInHand(this.PotionHand);
                Item PotionItem = PotionStack.getItem();
                float CloseEnoughDistance = PotionItem instanceof ThrowablePotionItem? this.maxAttackDistance:2;
                if(entity.distanceTo(target)>=CloseEnoughDistance){
                    BrainUtil.setWalkAndLookTargetMemories(entity, target, 1F, (int)CloseEnoughDistance);
                }

                else if(--this.rangedAttackTime <= 0) {
                    if (PotionItem instanceof ThrowablePotionItem) {
                        double d0 = entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
                        float f = MathHelper.sqrt(d0) / this.attackRadius;
                        this.throwPotion(entity, target, entity.getItemInHand(this.PotionHand));
                        entity.getItemInHand(this.PotionHand).shrink(1);
                        this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                    }
                    else{
                        List<EffectInstance> list = PotionUtils.getMobEffects(PotionStack);
                        for(EffectInstance effect : list){
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
                        Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(()->target), new PlaySoundPacket(soundevent, SoundCategory.PLAYERS, target.getX(),target.getY(),target.getZ(), 0.8F+(0.4F*MathUtil.rand.nextFloat()), 0.8F+(0.4F*MathUtil.rand.nextFloat())));
                        entity.swing(this.PotionHand);
                        entity.getItemInHand(this.PotionHand).shrink(1);
                        this.rangedAttackTime = 20;
                    }
                }
            });
        }
    }

    @Override
    protected void stop(@Nonnull ServerWorld p_212835_1_, @Nonnull AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        if(this.SwappedHand != null){
            this.ChangeItem(p_212835_2_, this.SwappedHand, p_212835_2_.getItemSwapIndex(this.SwappedHand),  true);
        }
        this.PotionHand=null;
        p_212835_2_.getBrain().eraseMemory(RegisterAI.HEAL_TARGET.get());
        this.rangedAttackTime = -1;
    }

    public void throwPotion(AbstractEntityCompanion entity, LivingEntity target, ItemStack potionStack) {
        Potion potion = PotionUtils.getPotion(potionStack);
        Vector3d vector3d = target.getDeltaMovement();
        double d0 = target.getX() + vector3d.x - entity.getX();
        double d1 = target.getEyeY() - (double)1.1F - entity.getY();
        double d2 = target.getZ() + vector3d.z - entity.getZ();
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2);

        PotionEntity potionentity = new PotionEntity(entity.getCommandSenderWorld(), entity);
        potionentity.setItem(PotionUtils.setPotion(potionStack, potion));
        potionentity.xRot -= -20.0F;
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

    private Optional<Hand> getPotioninHand(AbstractEntityCompanion entity) {
        for(Hand hand : Hand.values()) {
            ItemStack potion = entity.getItemInHand(hand);
            if (PotionUtils.getPotion(potion) == Potions.EMPTY) {
                continue;
            }
            List<EffectInstance> Effects = PotionUtils.getMobEffects(potion);
            if (!Effects.isEmpty()) {
                for (EffectInstance effect : Effects) {
                    if (effect.getEffect().getCategory() == EffectType.HARMFUL) {
                        return Optional.empty();
                    } else if (effect.getEffect() == HEAL || effect.getEffect() == REGENERATION) {
                        return Optional.of(hand);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private void ChangeItem(AbstractEntityCompanion entity, Hand hand, int index, boolean isResetting){
        ItemStack Buffer = entity.getItemInHand(hand);
        entity.setItemInHand(hand, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        this.ItemSwapDelay = 10;
        entity.setItemswapIndex(hand, isResetting? -1 : index);
        this.SwappedHand = isResetting? null : hand;
    }

}
