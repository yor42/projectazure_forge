package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import org.apache.commons.lang3.tuple.ImmutablePair;
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

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static net.minecraft.potion.Effects.INSTANT_HEALTH;
import static net.minecraft.potion.Effects.REGENERATION;

//Mostly from Guard Villager by seymourimadeit with minor tweak
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
        this.playerPredicate = (new EntityPredicate()).setDistance(15.0).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();

        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {

        if(this.ItemSwapDelay>0){
            this.ItemSwapDelay--;
        }

        if(this.companion.getOwner() != null && !this.companion.isSleeping() && !this.companion.isSitting() && this.ItemSwapDelay == 0) {

            Optional<Hand> PotionHand = this.getPotioninHand();
              List<LivingEntity> EntityAround = this.companion.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, this.companion.getBoundingBox().expand(20, 3, 20));
            Optional<ImmutablePair<Hand, LivingEntity>> Target = Optional.empty();
            PlayerEntity Owner = this.companion.getEntityWorld().getClosestPlayer(this.playerPredicate, this.companion);
            if (PotionHand.isPresent()) {
                if(Owner != null && this.companion.isOwner(Owner) && this.shouldHealEntity(Owner)){
                    Target = Optional.of(new ImmutablePair<>(PotionHand.get(), Owner));
                }
                else {
                    for (LivingEntity entity : EntityAround) {
                        //Undead is nearby. return false to prevent companion from accidentally healing the zombie
                        if (entity.isEntityUndead()) {
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
                if(Owner != null && this.companion.isOwner(Owner) && this.shouldHealEntity(Owner)){
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
        boolean isOwner = (entity instanceof PlayerEntity && this.companion.isOwner(entity));
        boolean isAlly = (this.companion.getOwner() != null && entity instanceof TameableEntity && ((TameableEntity) entity).isOwner(this.companion.getOwner()));
        if(isOwner || isAlly){
            return shouldHeal;
        }
        return false;
    }

    private void ChangeItem(Hand hand, int index, boolean isResetting){
        ItemStack Buffer = this.companion.getHeldItem(hand);
        this.companion.setHeldItem(hand, this.companion.getInventory().getStackInSlot(index));
        this.companion.getInventory().setStackInSlot(index, Buffer);
        this.ItemSwapDelay = 10;
        this.companion.setItemswapIndex(hand, isResetting? -1 : index);
        this.SwappedHand = isResetting? null : hand;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.TargeToHeal != null && this.shouldHealEntity(this.TargeToHeal);
    }

    @Override
    public void resetTask() {
        if(this.SwappedHand != null){
            this.ChangeItem(this.SwappedHand, this.companion.getItemSwapIndex(this.SwappedHand),  true);
        }
        this.TargeToHeal = null;
        this.tickInSight = 0;
        this.rangedAttackTime = -1;
    }

    @Override
    public void startExecuting() {
        if(this.TargeToHeal != null) {
            this.companion.getLookController().setLookPositionWithEntity(this.TargeToHeal, 10F, 10F);
        }
    }

    @Override
    public void tick() {
        if(this.PotionHand != null) {
            if (this.TargeToHeal == null)
                return;

            this.companion.getLookController().setLookPositionWithEntity(this.TargeToHeal, 10F, 10F);

            double d0 = this.companion.getDistanceSq(this.companion.getPosX(), this.companion.getPosY(), this.companion.getPosZ());
            boolean flag = this.companion.getEntitySenses().canSee(this.TargeToHeal);
            if (flag) {
                ++this.tickInSight;
            } else {
                this.tickInSight = 0;
            }
            if (!(d0 > this.maxAttackDistance) && this.tickInSight >= 5) {
                this.companion.getNavigator().clearPath();
            } else {
                this.companion.getNavigator().tryMoveToEntityLiving(this.companion, this.moveSpeed);
            }
            if (this.TargeToHeal.getDistance(this.companion) <= 3.0D) {
                companion.getMoveHelper().strafe(-0.5F, 0);
            }
            if (--this.rangedAttackTime == 0 && this.TargeToHeal.getHealth() < this.TargeToHeal.getMaxHealth() && this.TargeToHeal.isAlive()) {
                if (!flag) {
                    return;
                }
                float f = MathHelper.sqrt(d0) / this.attackRadius;
                float distanceFactor = MathHelper.clamp(f, 0.1F, 1.0F);
                this.throwPotion(this.TargeToHeal, PotionUtils.getPotionFromItem(this.companion.getHeldItem(this.PotionHand)), distanceFactor);
                this.companion.getHeldItem(this.PotionHand).shrink(1);
                this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
            } else if (this.rangedAttackTime < 0) {
                float f2 = MathHelper.sqrt((float) d0) / this.attackRadius;
                this.rangedAttackTime = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
            }
        }
    }

    public void throwPotion(LivingEntity target, Potion potion, float DistanceFactor) {
        if(potion != null) {
            Vector3d vector3d = target.getMotion();
            double d0 = target.getPosX() + vector3d.x - this.companion.getPosX();
            double d1 = target.getPosYEye() - (double)1.1F - this.companion.getPosY();
            double d2 = target.getPosZ() + vector3d.z - this.companion.getPosZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);

            PotionEntity potionentity = new PotionEntity(this.companion.getEntityWorld(), this.companion);
            potionentity.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potion));
            potionentity.rotationPitch -= -20.0F;
            potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
            this.companion.world.playSound((PlayerEntity)null, this.companion.getPosX(), this.companion.getPosY(), this.companion.getPosZ(), SoundEvents.ENTITY_WITCH_THROW, this.companion.getSoundCategory(), 1.0F, 0.8F + this.companion.getRNG().nextFloat() * 0.4F);
            this.companion.swing(this.PotionHand, true);
            this.companion.world.addEntity(potionentity);
        }
    }

    private int getPotionFromInv(LivingEntity HealTarget){
        Effect preferredEffect = HealTarget.getHealth()<6? INSTANT_HEALTH:REGENERATION;
        int index = -1;
        boolean foundPotion = false;
        for(int i = 0; i <this.companion.getInventory().getSlots(); i++){
            ItemStack stack = this.companion.getInventory().getStackInSlot(i);
            if(stack.getItem() instanceof SplashPotionItem){
                List<EffectInstance> Effects = PotionUtils.getEffectsFromStack(stack);
                if(!Effects.isEmpty()){
                    for(EffectInstance effect : Effects){
                        if(effect.getPotion().getEffectType() == EffectType.HARMFUL){
                            return -1;
                        }
                        else if(effect.getPotion() == preferredEffect){
                            return i;
                        }
                        else if(!foundPotion && effect.getPotion() == INSTANT_HEALTH || effect.getPotion() == REGENERATION){
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
            if(this.companion.getHeldItem(hand).getItem() instanceof SplashPotionItem){
                List<EffectInstance> Effects = PotionUtils.getEffectsFromStack(this.companion.getHeldItem(hand));
                if(!Effects.isEmpty()){
                    for(EffectInstance effect : Effects){
                        if(effect.getPotion().getEffectType() == EffectType.HARMFUL){
                            return Optional.empty();
                        }
                        else if(effect.getPotion() == INSTANT_HEALTH || effect.getPotion() == REGENERATION){
                            return Optional.of(hand);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
