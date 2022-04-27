package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.EnumSet;

import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class CompanionUseCrossbowGoal<T extends AbstractEntityCompanion & RangedAttackMob & CrossbowAttackMob> extends Goal {
    public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);
    private final T mob;
    private CrossbowState crossbowState = CrossbowState.UNCHARGED;
    private final double speedModifier;
    private final float attackRadiusSqr;
    private int seeTime;
    ItemStack AmmoStack = ItemStack.EMPTY;
    private int attackDelay;
    private int updatePathDelay;

    public CompanionUseCrossbowGoal(T entity, double attackSpeed, float attackRange){
        this.mob = entity;
        this.speedModifier = attackSpeed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.getAmmo();
        return this.mob.canUseCrossbow && this.isValidTarget() && this.isHoldingCrossbow() && !this.AmmoStack.isEmpty();
    }

    private void getAmmo() {
        for(int i=0; i<this.mob.getAmmoStorage().getSlots(); i++){
            if(this.mob.getAmmoStorage().getStackInSlot(i).getItem() instanceof ArrowItem){
                this.AmmoStack = this.mob.getAmmoStorage().getStackInSlot(i);
                return;
            }
        };
        this.AmmoStack = ItemStack.EMPTY;
    }

    private boolean isHoldingCrossbow() {
        return this.mob.getItemInHand(MAIN_HAND).getItem() instanceof CrossbowItem;
    }

    private boolean isValidTarget() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    public boolean canContinueToUse() {
        return this.isValidTarget() && (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingCrossbow();
    }

    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.mob.setTarget(null);
        this.seeTime = 0;
        if (this.mob.isUsingItem()) {
            this.mob.stopUsingItem();
            this.mob.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.mob.getUseItem(), false);
        }

    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            double d0 = this.mob.distanceToSqr(livingentity);
            boolean flag2 = (d0 > (double)this.attackRadiusSqr || this.seeTime < 5) && this.attackDelay == 0;
            if (flag2) {
                --this.updatePathDelay;
                if (this.updatePathDelay <= 0) {
                    this.mob.getNavigation().moveTo(livingentity, this.canRun() ? this.speedModifier : this.speedModifier * 0.5D);
                    this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(this.mob.getRandom());
                }
            } else {
                this.updatePathDelay = 0;
                this.mob.getNavigation().stop();
            }

            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            if (this.crossbowState == CrossbowState.UNCHARGED) {
                if (!flag2) {
                    this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
                    this.crossbowState = CrossbowState.CHARGING;
                    this.mob.setChargingCrossbow(true);
                }
            } else if (this.crossbowState == CrossbowState.CHARGING) {
                if (!this.mob.isUsingItem()) {
                    this.crossbowState = CrossbowState.UNCHARGED;
                }

                int i = this.mob.getTicksUsingItem();
                ItemStack itemstack = this.mob.getUseItem();
                if (i >= CrossbowItem.getChargeDuration(itemstack)) {
                    this.mob.releaseUsingItem();
                    this.crossbowState = CrossbowState.CHARGED;
                    ItemStack itemstack1 = this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
                    this.tryLoadProjectiles(this.mob, itemstack1);
                    this.attackDelay = 20 + this.mob.getRandom().nextInt(20);
                    this.mob.setChargingCrossbow(false);
                }
            } else if (this.crossbowState == CrossbowState.CHARGED) {
                --this.attackDelay;
                if (this.attackDelay == 0) {
                    this.crossbowState = CrossbowState.READY_TO_ATTACK;
                }
            } else if (this.crossbowState == CrossbowState.READY_TO_ATTACK && flag) {
                this.mob.performCrossbowAttack(livingentity, 1.0F);
                ItemStack itemstack1 = this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
                CrossbowItem.setCharged(itemstack1, false);
                this.crossbowState = CrossbowState.UNCHARGED;
            }

        }
    }

    private boolean tryLoadProjectiles(T p_220021_0_, ItemStack p_220021_1_) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, p_220021_1_);
        int j = i == 0 ? 1 : 3;
        ItemStack itemstack = p_220021_0_.getProjectile(p_220021_1_);
        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (!loadProjectile(p_220021_0_, p_220021_1_, itemstack, k > 0, false)) {
                return false;
            }
        }

        return true;
    }

    private boolean loadProjectile(T p_220023_0_, ItemStack p_220023_1_, ItemStack stack, boolean p_220023_3_, boolean p_220023_4_) {
        if (stack.isEmpty()) {
            return false;
        } else {
            boolean flag = p_220023_4_ && stack.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !p_220023_4_ && !p_220023_3_) {
                itemstack = stack.split(1);
                for(int i=0; i<p_220023_0_.getAmmoStorage().getSlots(); i++){
                    if(p_220023_0_.getAmmoStorage().getStackInSlot(i) == stack){
                        p_220023_0_.getAmmoStorage().setStackInSlot(i, ItemStack.EMPTY);
                        break;
                    }
                }
            } else {
                itemstack = stack.copy();
            }

            addChargedProjectile(p_220023_1_, itemstack);
            return true;
        }
    }

    public static void setCharged(ItemStack p_220011_0_, boolean p_220011_1_) {
        CompoundTag compoundnbt = p_220011_0_.getOrCreateTag();
        compoundnbt.putBoolean("Charged", p_220011_1_);
    }

    private static void addChargedProjectile(ItemStack p_220029_0_, ItemStack p_220029_1_) {
        CompoundTag compoundnbt = p_220029_0_.getOrCreateTag();
        ListTag listnbt;
        if (compoundnbt.contains("ChargedProjectiles", 9)) {
            listnbt = compoundnbt.getList("ChargedProjectiles", 10);
        } else {
            listnbt = new ListTag();
        }

        CompoundTag compoundnbt1 = new CompoundTag();
        p_220029_1_.save(compoundnbt1);
        listnbt.add(compoundnbt1);
        compoundnbt.put("ChargedProjectiles", listnbt);
    }

    private boolean canRun() {
        return this.crossbowState == CrossbowState.UNCHARGED;
    }

    enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;
    }
}
