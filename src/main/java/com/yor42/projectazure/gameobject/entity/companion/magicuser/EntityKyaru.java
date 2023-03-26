package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.projectiles.EntitySpellBall;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;

import static net.minecraft.util.Hand.MAIN_HAND;

public class EntityKyaru extends AbstractCompanionMagicUser {
    public EntityKyaru(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.PRICONNE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat_tail", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            event.getController().setAnimation(builder.addAnimation("sit_tail").addAnimation("sit_tail_idle"));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_tail", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving() && this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_tail", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_tail", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("idle_tail", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }
        else if(this.isDeadOrDying()){
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
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));

            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == MAIN_HAND){
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
        }
        else if(this.isDeadOrDying()){
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
        if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving() && this.getVehicle() == null) {
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
        return enums.CompanionRarity.STAR_3;
    }

    @Override
    public int getInitialSpellDelay() {
        return 26;
    }

    @Override
    public int getProjectilePreAnimationDelay() {
        return 15;
    }

    @Override
    public InteractionHand getSpellUsingHand() {
        return InteractionHand.OFF_HAND;
    }

    @Override
    public boolean shouldUseSpell(LivingEntity Target) {
        return !isSwimming() && this.getMainHandItem().getItem() == RegisterItems.KYARU_STAFF.get();
    }

    @Override
    public void ShootProjectile(Level world, @Nonnull LivingEntity target) {
        if(target.isAlive()){
            double x = target.getX() - (this.getX());
            double y = target.getEyeY() - (this.getY(0.7));
            double z = target.getZ() - (this.getZ());

            EntitySpellBall projectile = new EntitySpellBall(this.getCommandSenderWorld(), this);
            projectile.shoot(x,y,z, 1.1F, 0.05F);
            projectile.setPos(this.getX(), this.getY(0.7), this.getZ());
            this.getCommandSenderWorld().addFreshEntity(projectile);

            this.addExp(0.2F);
            this.addExhaustion(0.05F);
            this.addMorale(-0.2);
        }
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.KyaruMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.KyaruSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.KyaruHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.KyaruAttackDamage.get())
                ;
    }
}
