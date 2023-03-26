package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.libs.enums;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.yor42.projectazure.setup.register.RegisterItems.SOULUMSWORD;

public class EntityHaru extends AbstractSwordUserBase{
    public EntityHaru(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.SOULWORKER;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        super.registerControllers(animationData);
        animationData.addAnimationController(new AnimationController<>(this, "controller_cloth", 10, this::predicate_cloth));
    }

    private <T extends IAnimatable> PlayState predicate_cloth(AnimationEvent<T> tAnimationEvent) {
        AnimationBuilder builder = new AnimationBuilder();
        tAnimationEvent.getController().setAnimation(builder.addAnimation("cloth_wave"));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }
        else  if(this.isDeadOrDying()){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_loop"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd_chest", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));

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
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null) {
            if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_arm").addAnimation("sit_arm_idle"));
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
        else if (isMoving()) {
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
        }
        else if(this.isDeadOrDying()){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_loop"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit").addAnimation("sit_leg_idle"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
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
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        event.getController().setAnimation(builder.addAnimation("hair_wave", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_4;
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 20;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Arrays.asList(3, 14));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(SOULUMSWORD.get()));
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 3;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        target.knockback(AttackCount==0?0.1F:0.3F, Mth.sin(this.yRot * ((float)Math.PI / 180F)), -Mth.cos(this.yRot * ((float)Math.PI / 180F)));
        target.hurt(DamageSource.mobAttack(this), damage);
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.HaruMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.HaruSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.HaruHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.HaruAttackDamage.get())
                ;
    }

}
