package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.*;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static net.minecraft.potion.Effects.HEAL;
import static net.minecraft.potion.Effects.REGENERATION;

public class CompanionHealOwnerAndAllyGoal extends Goal {

    private final AbstractEntityCompanion companion;
    @Nullable
    private LivingEntity TargeToHeal;
    private int rangedAttackTime = -1;
    private int tickInSight;
    private int ItemSwapDelay = 0;
    private final double moveSpeed;
    private final int maxRangedAttackTime, attackIntervalMin;
    private final float attackRadius;
    private Hand PotionHand;
    @Nullable
    private Hand SwappedHand;
    private final float maxAttackDistance;
    private final EntityPredicate playerPredicate;

    public CompanionHealOwnerAndAllyGoal(AbstractEntityCompanion companion, int maxRangedAttackTime, int attackIntervalMin, double moveSpeed, float attackRadius){
        this.companion = companion;
        this.moveSpeed = moveSpeed;
        this.maxRangedAttackTime = maxRangedAttackTime;
        this.attackIntervalMin = attackIntervalMin;
        this.attackRadius = attackRadius;
        this.maxAttackDistance = attackRadius*attackRadius;
        this.playerPredicate = (new EntityPredicate()).range(15.0).allowInvulnerable().allowSameTeam().allowNonAttackable();

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {

        if(this.ItemSwapDelay>0){
            this.ItemSwapDelay--;
        }

        if(this.companion.isCriticallyInjured()){
            return false;
        }

        if(this.companion.getOwner() != null && !this.companion.isSleeping() && !this.companion.isOrderedToSit() && this.ItemSwapDelay == 0) {

            Optional<Hand> PotionHand = this.getPotioninHand();
              List<LivingEntity> EntityAround = this.companion.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, this.companion.getBoundingBox().expandTowards(20, 3, 20));
            Optional<ImmutablePair<Hand, LivingEntity>> Target = Optional.empty();
            PlayerEntity Owner = this.companion.getCommandSenderWorld().getNearestPlayer(this.playerPredicate, this.companion);
            if (PotionHand.isPresent()) {
                if(Owner != null && this.companion.isOwnedBy(Owner) && this.shouldHealEntity(Owner)){
                    Target = Optional.of(new ImmutablePair<>(PotionHand.get(), Owner));
                }
                else {
                    for (LivingEntity entity : EntityAround) {
                        //Undead is nearby. return false to prevent companion from accidentally healing the zombie
                        if (entity.isInvertedHealAndHarm()) {
                            return false;
                        } else if (this.shouldHealEntity(entity)) {
                            Target = Optional.of(new ImmutablePair<>(PotionHand.get(), entity));
                        }
                    }
                }
                if(Target.isPresent()){
                    this.PotionHand = Target.get().getKey();
                    this.TargeToHeal = Target.get().getValue();
                    return true;
                }
            }
            else{
                int inventoryoindex = -1;
                if(Owner != null && this.companion.isOwnedBy(Owner) && this.shouldHealEntity(Owner)){
                    inventoryoindex = this.getPotionFromInv(Owner);
                }
                else {
                    for (LivingEntity entity : EntityAround) {
                        if (this.shouldHealEntity(entity)) {
                            inventoryoindex = this.getPotionFromInv(entity);
                            break;
                        }
                    }
                }

                if(inventoryoindex > -1){
                    if(this.companion.getFreeHand().isPresent()){
                        this.ChangeItem(this.companion.getFreeHand().get(), inventoryoindex, false);
                    }
                }
            }
        }
        this.TargeToHeal = null;
        return false;
    }

    private boolean shouldHealEntity(LivingEntity entity){

        if(entity == this.companion){
            return false;
        }

        boolean shouldHeal = entity.isAlive() && entity.getHealth() < entity.getMaxHealth();
        boolean isOwner = (entity instanceof PlayerEntity && this.companion.isOwnedBy(entity));
        boolean isAlly = (this.companion.getOwner() != null && entity instanceof TameableEntity && ((TameableEntity) entity).isOwnedBy(this.companion.getOwner()));
        if(isOwner || isAlly){
            return shouldHeal;
        }
        return false;
    }

    private void ChangeItem(Hand hand, int index, boolean isResetting){
        ItemStack Buffer = this.companion.getItemInHand(hand);
        this.companion.setItemInHand(hand, this.companion.getInventory().getStackInSlot(index));
        this.companion.getInventory().setStackInSlot(index, Buffer);
        this.ItemSwapDelay = 10;
        this.companion.setItemswapIndex(hand, isResetting? -1 : index);
        this.SwappedHand = isResetting? null : hand;
    }

    @Override
    public boolean canContinueToUse() {
        return this.TargeToHeal != null && this.shouldHealEntity(this.TargeToHeal);
    }

    @Override
    public void stop() {
        if(this.SwappedHand != null){
            this.ChangeItem(this.SwappedHand, this.companion.getItemSwapIndex(this.SwappedHand),  true);
        }
        this.TargeToHeal = null;
        this.tickInSight = 0;
        this.rangedAttackTime = -1;
    }

    @Override
    public void start() {
        if(this.TargeToHeal != null) {
            this.companion.getLookControl().setLookAt(this.TargeToHeal, 10F, 10F);
        }
    }

    @Override
    public void tick() {
        if(this.PotionHand != null) {
            if (this.TargeToHeal == null)
                return;

            this.companion.getLookControl().setLookAt(this.TargeToHeal, 10F, 10F);

            double d0 = this.companion.distanceToSqr(this.companion.getX(), this.companion.getY(), this.companion.getZ());
            boolean flag = this.companion.getSensing().canSee(this.TargeToHeal);
            if (flag) {
                ++this.tickInSight;
            } else {
                this.tickInSight = 0;
            }
            if (!(d0 > this.maxAttackDistance) && this.tickInSight >= 5) {
                this.companion.getNavigation().stop();
            } else {
                this.companion.getNavigation().moveTo(this.companion, this.moveSpeed);
            }
            if (this.TargeToHeal.distanceTo(this.companion) <= 3.0D) {
                companion.getMoveControl().strafe(-0.5F, 0);
            }
            if (--this.rangedAttackTime == 0 && this.TargeToHeal.getHealth() < this.TargeToHeal.getMaxHealth() && this.TargeToHeal.isAlive()) {
                if (!flag) {
                    return;
                }
                float f = MathHelper.sqrt(d0) / this.attackRadius;
                float distanceFactor = MathHelper.clamp(f, 0.1F, 1.0F);
                this.throwPotion(this.TargeToHeal, PotionUtils.getPotion(this.companion.getItemInHand(this.PotionHand)), distanceFactor);
                this.companion.getItemInHand(this.PotionHand).shrink(1);
                this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
            } else if (this.rangedAttackTime < 0) {
                float f2 = MathHelper.sqrt((float) d0) / this.attackRadius;
                this.rangedAttackTime = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
            }
        }
    }

    public void throwPotion(LivingEntity target, Potion potion, float DistanceFactor) {
        if(potion != null) {
            Vector3d vector3d = target.getDeltaMovement();
            double d0 = target.getX() + vector3d.x - this.companion.getX();
            double d1 = target.getEyeY() - (double)1.1F - this.companion.getY();
            double d2 = target.getZ() + vector3d.z - this.companion.getZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);

            PotionEntity potionentity = new PotionEntity(this.companion.getCommandSenderWorld(), this.companion);
            potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
            potionentity.xRot -= -20.0F;
            potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
            this.companion.level.playSound((PlayerEntity)null, this.companion.getX(), this.companion.getY(), this.companion.getZ(), SoundEvents.WITCH_THROW, this.companion.getSoundSource(), 1.0F, 0.8F + this.companion.getRandom().nextFloat() * 0.4F);
            this.companion.swing(this.PotionHand, true);
            this.companion.level.addFreshEntity(potionentity);
        }
    }

    private int getPotionFromInv(LivingEntity HealTarget){
        Effect preferredEffect = HealTarget.getHealth()<6? HEAL:REGENERATION;
        int index = -1;
        boolean foundPotion = false;
        for(int i = 0; i <this.companion.getInventory().getSlots(); i++){
            ItemStack stack = this.companion.getInventory().getStackInSlot(i);
            if(stack.getItem() instanceof SplashPotionItem){
                List<EffectInstance> Effects = PotionUtils.getMobEffects(stack);
                if(!Effects.isEmpty()){
                    for(EffectInstance effect : Effects){
                        if(effect.getEffect().getCategory() == EffectType.HARMFUL){
                            return -1;
                        }
                        else if(effect.getEffect() == preferredEffect){
                            return i;
                        }
                        else if(!foundPotion && effect.getEffect() == HEAL || effect.getEffect() == REGENERATION){
                            index = i;
                            foundPotion = true;
                        }
                    }
                }
            }
        }
        return index;
    }

    private Optional<Hand> getPotioninHand() {
        for(Hand hand : Hand.values()){
            if(this.companion.getItemInHand(hand).getItem() instanceof SplashPotionItem){
                List<EffectInstance> Effects = PotionUtils.getMobEffects(this.companion.getItemInHand(hand));
                if(!Effects.isEmpty()){
                    for(EffectInstance effect : Effects){
                        if(effect.getEffect().getCategory() == EffectType.HARMFUL){
                            return Optional.empty();
                        }
                        else if(effect.getEffect() == HEAL || effect.getEffect() == REGENERATION){
                            return Optional.of(hand);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
