package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.items.IItemHandlerModifiable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityEnterprise extends EntityKansenAircraftCarrier implements IAzurLaneKansen {

    protected static final DataParameter<Integer> INVINCIBLE_TIMER = EntityDataManager.defineId(EntityEnterprise.class, DataSerializers.INT);

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();
        if(this.isDeadOrDying()){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_idle"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("idle_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("sit_injured_idle", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(builder.addAnimation("animation.enterprise.sit_start").addAnimation("animation.enterprise.sit", ILoopType.EDefaultLoopTypes.LOOP));
                }
            }
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.sail", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.run", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.walk", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    public EntityEnterprise(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();
        if(this.isDeadOrDying()){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_idle"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.sleep", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                    event.getController().setAnimation(builder.addAnimation("animation.enterprise.idle", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            return PlayState.STOP;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            if(this.isOrderedToSit())
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat_sit", ILoopType.EDefaultLoopTypes.LOOP));
            else
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }
        else if(this.isReloadingMainHand()) {
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                
                event.getController().setAnimation(builder.addAnimation("gun_reload_onehanded", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_shoot_onehanded", ILoopType.EDefaultLoopTypes.LOOP));
            }

            return PlayState.CONTINUE;
        }
        else if(this.shouldPlayShipAttackAnim()){
            event.getController().setAnimation(builder.addAnimation("ship_fire"));
            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.getUsedItemHand() == Hand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.heal_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.swim_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.sail_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.run_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.walk_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_onehanded", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("animation.enterprise.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected float getBowInaccuracy() {
        return 2;
    }

    @Override
    public SoundEvent getDisappointedAmbientSound() {
        return registerSounds.ENTERPRISE_TALK_DISAPPOINTED;
    }

    @Override
    public SoundEvent getStrangerAmbientSound() {
        return registerSounds.ENTERPRISE_TALK_STRANGER;
    }

    @Override
    public SoundEvent getFriendlyAmbientSound() {
        return registerSounds.ENTERPRISE_TALK_FRIENDLY;
    }

    @Override
    public SoundEvent getLikeAmbientSound() {
        return registerSounds.ENTERPRISE_TALK_CRUSH;
    }

    @Override
    public SoundEvent getLoveAmbientSound() {
        return registerSounds.ENTERPRISE_TALK_LOVE;
    }

    @Nullable
    @Override
    public SoundEvent getOathSound() {
        return registerSounds.ENTERPRISE_TALK_OATH;
    }


    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.ENTERPRISE_TALK_ATTACK;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.ENTERPRISE_TALK_PAT;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.EnterpriseMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.EnterpriseSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.EnterpriseHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.EnterpriseAttackDamage.get())
                ;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if(this.getInvincibleTimer()>0){
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        int InvincibleTimer = this.getInvincibleTimer();
        if(InvincibleTimer>0){
            this.setInvincibleTimer(InvincibleTimer-1);
        }

        if (this.level.isClientSide && this.getInvincibleTimer()>0 && this.tickCount%10 == 0) {
            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (MathUtil.getRand().nextDouble() - 0.5D) * 2.0D, -MathUtil.getRand().nextDouble(), (MathUtil.getRand().nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    @Override
    public void LaunchPlane(ItemStack planestack, AbstractEntityPlanes plane, LivingEntity target, IItemHandlerModifiable hanger, int hangerIndex) {
        if(this.getRandom().nextFloat()<0.4F){
            this.setInvincibleTimer(240);
            plane.setAttackDamage(plane.getAttackDamage()*2);
            for(int i = 0; i < 5; ++i) {
                double d0 = MathUtil.getRand().nextGaussian() * 0.02D;
                double d1 = MathUtil.getRand().nextGaussian() * 0.02D;
                double d2 = MathUtil.getRand().nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
            }
        }
        super.LaunchPlane(planestack, plane, target, hanger, hangerIndex);
    }

    private void setInvincibleTimer(int invincibleTimer) {
        this.entityData.set(INVINCIBLE_TIMER, invincibleTimer);
    }

    private int getInvincibleTimer() {
        return this.entityData.get(INVINCIBLE_TIMER);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(INVINCIBLE_TIMER, 0);
    }
}
