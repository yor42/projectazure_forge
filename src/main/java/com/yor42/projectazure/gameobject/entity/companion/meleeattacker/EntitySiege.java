package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yor42.projectazure.setup.register.RegisterItems.WARHAMMER;

public class EntitySiege extends AbstractSwordUserBase implements IAknOp {
    public EntitySiege(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<EntitySiege> UpperBodycontroller = new AnimationController<>(this, "controller_lowerbody", 0, this::predicate_lowerbody);
        animationData.addAnimationController(UpperBodycontroller);
        animationData.addAnimationController(new AnimationController<>(this, "controller_upperbody", 0, this::predicate_upperbody));
        animationData.addAnimationController(new AnimationController<>(this, "controller_head", 0, this::predicate_head));
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
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
            return PlayState.STOP;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.getSkillAnimationTime()>0){
            event.getController().setAnimation(builder.addAnimation("leaping_hammer_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));

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
                if (this.getMainHandItem().getItem() instanceof TieredItem) {
                    event.getController().setAnimation(builder.addAnimation("sit_idle_arm_toolmainhand", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_idle_arm_emptymainhand", ILoopType.EDefaultLoopTypes.LOOP));
                }
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
        else if (isMoving()&& this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            return PlayState.STOP;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    public boolean canUseSkill(@Nullable LivingEntity target){
        return !this.isAnimating() && this.getTarget() != null && this.getTarget().isAlive() && this.getSkillPoints()>=12 && this.isSkillItem(this.getMainHandItem());
    }

    @Override
    public boolean isSkillItem(ItemStack stack) {
        return this.isTalentedWeapon(stack);
    }

    @Override
    public boolean performOneTimeSkill(LivingEntity target) {
        boolean val = this.getTarget() != null && this.getTarget().isAlive() && this.getSkillPoints()>=12;
        return !val;
    }

    @Override
    public boolean performSkillTick(LivingEntity target, int Timer) {
        if(target != null && this.getOwner() != null){
            if(Timer == 0){
                this.setSkillAnimationTime(this.MeleeAttackAnimationLength());
                this.playSound(registerSounds.LEAPHAMMER_START, 1, 0.8F+(0.2F*this.getRandom().nextFloat()));
            }
            else if(this.getAttackDamageDelay().contains(Timer)){
                List<Entity> AttackTarget = this.getLevel().getEntities(this, this.getBoundingBox().inflate(2, 2, 2), (entity) -> entity instanceof LivingEntity && (!EntitySiege.this.isOwnedBy((LivingEntity) entity) && !(entity instanceof TamableAnimal && ((TamableAnimal) entity).isOwnedBy(EntitySiege.this.getOwner()))) && (PAConfig.CONFIG.EnablePVP.get() && entity instanceof Player));
                for(Entity entity : AttackTarget){
                    if(entity instanceof LivingEntity){
                        entity.hurt(DamageSource.mobAttack(this), this.getAttackDamageMainHand()*2.2F);
                    }
                }
                this.playSound(registerSounds.LEAPHAMMER_HIT, 1, 0.8F+(0.2F*this.getRandom().nextFloat()));
                this.addSkillPoints(-12);
                return true;
            }
        }
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(this.tickCount%20==0){
            this.addSkillPoints();
        }
    }

    @Override
    public int maxSkillPoint() {
        return 24;
    }

    @Override
    public ArrayList<Integer> MeleeAttackAudioCue() {
        return new ArrayList<>(Collections.singletonList(12));
    }
    @Override
    public void playMeleeAttackPreSound() {
        this.playSound(registerSounds.WARHAMMER_SWING, 1, 0.8F+(0.2F*this.getRandom().nextFloat()));
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        event.getController().setAnimation(builder.addAnimation("idle_chest", ILoopType.EDefaultLoopTypes.LOOP));
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
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_idle_leg"));
                }
            return PlayState.CONTINUE;
        }
        else if(this.getSkillAnimationTime()>0){
            event.getController().setAnimation(builder.addAnimation("leaping_hammer_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
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

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_6;
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 26;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Collections.singletonList(12));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(WARHAMMER.get()));
    }

    @Override
    public boolean isDuelWielding() {
        return false;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 3;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        target.hurt(DamageSource.mobAttack(this), damage);
        target.playSound(registerSounds.WARHAMMER_HIT, 1, 0.8F+(0.2F*this.getRandom().nextFloat()));
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.VANGUARD;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.SiegeMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.SiegeSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.SiegeHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.SiegeAttackDamage.get())
                ;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return registerSounds.SIEGE_TALK_NORMAL;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.SIEGE_TALK_PAT;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.SIEGE_TALK_ATTACK;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return registerSounds.SIEGE_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return registerSounds.SIEGE_TALK_HIGH_AFFECTION2;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return registerSounds.SIEGE_TALK_HIGH_AFFECTION3;
    }
}
