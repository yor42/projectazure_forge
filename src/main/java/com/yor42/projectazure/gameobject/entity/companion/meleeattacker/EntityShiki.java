package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IFGOServant;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;

import static com.yor42.projectazure.setup.register.registerSounds.*;

public class EntityShiki extends AbstractEntityCompanion implements IMeleeAttacker, IFGOServant {
    public EntityShiki(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.SERVANT;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_idle"));
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            //event.getController().setAnimation(builder.addAnimation(this.swingingArm == Hand.MAIN_HAND?"swingL":"swingR", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.STOP;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd_chest", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));

            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == InteractionHand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.getUsedItemHand() == InteractionHand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", ILoopType.EDefaultLoopTypes.LOOP));
            }

            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }
        if(this.isOrderedToSit()|| this.getVehicle() != null){
            if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("sit_arm_idle", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            return PlayState.STOP;
        }
        else if (isMoving()&& this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_leg"));
            return PlayState.CONTINUE;
        }else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_idle"));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_leg_idle"));
                }
            return PlayState.CONTINUE;
        }
        if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (isMoving() && this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if(this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(LivingEntity::isDeadOrDying).orElse(true) && this.getMainHandItem().getItem() == RegisterItems.KITCHEN_KNIFE.get()){
            int swapindex = this.getItemSwapIndex(InteractionHand.MAIN_HAND);
            if(swapindex>-1){
                this.setItemInHand(InteractionHand.MAIN_HAND, this.getInventory().extractItem(swapindex, this.getInventory().getStackInSlot(swapindex).getCount(), false));
                this.setItemSwapIndexMainHand(-1);
            }
            else{
                this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
        }
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_4;
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 34;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(23);
        return list;
    }


    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(RegisterItems.KITCHEN_KNIFE.get()));
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return registerSounds.SHIKI_TALK_DIE;
    }

    @Override
    public boolean hasMeleeItem() {
        return false;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 2;
    }

    @Override
    public boolean shouldUseNonVanillaAttack(LivingEntity target) {
        ItemStack MainHandItem = this.getMainHandItem();

        if(this.getTalentedWeaponList().contains(MainHandItem.getItem())){
            return true;
        }

        this.SwitchItem();
        return false;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        if(AttackCount == 1 && MathUtil.rollBooleanRNG(0.25F)){
            this.playSound(SHIKI_TALK_ATTACK, this.getSoundVolume(), this.getVoicePitch());
        }
        target.hurt(DamageSource.mobAttack(this), damage*1.5F);
    }

    @Override
    public void awardKillScore(@Nonnull Entity p_191956_1_, int p_191956_2_, @Nonnull DamageSource p_191956_3_) {
        super.awardKillScore(p_191956_1_, p_191956_2_, p_191956_3_);
        this.playSound(SHIKI_TALK_KILL, this.getSoundVolume(), this.getVoicePitch());
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        if(this.getRandom().nextFloat()<0.2F){
            return SHIKI_TALK_HURT;
        }
        return super.getHurtSound(damageSourceIn);
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return SHIKI_TALK_AGGRO;
    }

    @Override
    public SoundEvent getBondlevel1Sound() {
        return SHIKI_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getBondlevel2Sound() {
        return SHIKI_TALK_HIGH_AFFECTION2;
    }
    @Override
    public SoundEvent getBondlevel3Sound() {
        return SHIKI_TALK_HIGH_AFFECTION3;
    }
    @Override
    public SoundEvent getBondlevel4Sound() {
        return SHIKI_TALK_HIGH_AFFECTION4;
    }
    @Override
    public SoundEvent getBondlevel5Sound() {
        return SHIKI_TALK_HIGH_AFFECTION5;
    }

    @Override
    public SoundEvent getNormalAmbientSound() {
        return SHIKI_TALK_NORMAL;
    }
    @Override
    public enums.SERVANT_CLASSES getServantClass() {
        return enums.SERVANT_CLASSES.ASSASSIN;
    }

    @Nonnull
    @Override
    public ItemStack createWeaponStack() {
        ItemStack stack = new ItemStack(RegisterItems.KITCHEN_KNIFE.get());
        stack.getOrCreateTag().putUUID("owner", this.getUUID());
        return stack;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ShikiMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ShikiSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.ShikiHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ShikiAttackDamage.get())
                ;
    }
}
