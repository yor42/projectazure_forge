package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityFallingSword;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
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
import java.util.Optional;

public class EntityTexas extends AbstractSwordUserBase implements IAknOp {

    private final List<LivingEntity> targets = new ArrayList<>();
    private final List<Entity> projectiles = new ArrayList<>();
    public EntityTexas(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
    }

    @Override
    protected Optional<SoundEvent> getLimitBreakSound() {

        if(this.getLimitBreakLv() == 1){
            return Optional.of(registerSounds.TEXAS_PROMOTION_1);
        }

        return Optional.of(registerSounds.TEXAS_PROMOTION_2);
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
        return 40;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        super.registerControllers(animationData);
        animationData.addAnimationController(new AnimationController<>(this, "controller_tail", 10, this::predicate_tail));
    }

    private <T extends IAnimatable> PlayState predicate_tail(AnimationEvent<T> event) {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOrderedToSit() || (this.getVehicle() != null && this.getVehicle() != this.getOwner())){
            event.getController().setAnimation(builder.addAnimation("sit_tail").addAnimation("sit_tail_idle"));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat_tail", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
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
            event.getController().setAnimation(builder.addAnimation("lewd_chest", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.getSkillAnimationTime()>0){
            event.getController().setAnimation(builder.addAnimation("skill_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
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
                if (this.getMainHandItem().getItem() instanceof TieredItem) {
                    event.getController().setAnimation(builder.addAnimation("sit_arm_idle_toolhand", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_arm_idle_emptyhand", ILoopType.EDefaultLoopTypes.LOOP));
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

    @Override
    public ArrayList<Integer> MeleeAttackAudioCue() {
        return new ArrayList<>(Collections.singletonList(5));
    }

    @Override
    public void playMeleeAttackPreSound() {
        this.playSound(registerSounds.TEXAS_SWORD_SWING, 1, 0.8F+(0.2F*this.getRandom().nextFloat()));
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent) {
        AnimationBuilder builder = new AnimationBuilder();
        pAnimationEvent.getController().setAnimation(builder.addAnimation("idle_chest", ILoopType.EDefaultLoopTypes.LOOP));
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
        else if(this.getSkillAnimationTime()>0){
            event.getController().setAnimation(builder.addAnimation("skill_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
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


    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 18;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Collections.singletonList(7));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(RegisterItems.FLEXABLE_SWORD_THINGY.get()));
    }

    @Override
    public boolean canUseSkill(@Nullable LivingEntity target) {
        int currentspelldelay = this.getNonVanillaMeleeAttackDelay();

        if(target == null){
            return false;
        }
        int sp = this.getSkillPoints();
        boolean hasSkillpoint = sp>39;
        if(currentspelldelay == 0 &&hasSkillpoint){
            this.targets.clear();
            this.targets.add(target);
            this.getCommandSenderWorld().getEntities(target, target.getBoundingBox().inflate(5, 2, 5), (canditate)-> canditate instanceof LivingEntity && !this.isAlly((LivingEntity) canditate)).forEach((entity)-> this.targets.add((LivingEntity) entity));
            return !this.targets.isEmpty() && this.getTalentedWeaponList().contains(this.getOffhandItem().getItem());
        }
        return false;
    }

    @Override
    public boolean performOneTimeSkill(LivingEntity target) {
        return false;
    }

    //Sword Rain :d
    @Override
    public boolean performSkillTick(LivingEntity target, int Timer) {
        if(Timer == 0){
            this.projectiles.clear();
            this.setSkillAnimationTime(44);
        }else if(Timer == 15) {
            int count = MathUtil.generateRandomIntInRange(9,14);
            int index = 0;
            while(index<count){
                if(index<this.targets.size()){
                    LivingEntity tgt = this.targets.get(index);
                    EntityFallingSword sword = new EntityFallingSword(this, tgt.getX(0.5), this.getY()+15, tgt.getZ(0.5), this.getCommandSenderWorld());
                    sword.shoot(0, -20, 0,2, 0.5F);
                    this.projectiles.add(sword);
                }
                else{
                    float randomx = (float) ((target.getX(0.5)-5)+(MathUtil.getRand().nextFloat()*10));
                    float randomy = (float) (target.getY()+15);
                    float randomz = (float) ((target.getZ(0.5)-5)+(MathUtil.getRand().nextFloat()*10));
                    EntityFallingSword sword = new EntityFallingSword(this, randomx, randomy, randomz, this.getCommandSenderWorld());
                    sword.shoot(0, -20, 0,2, 0.5F);
                    this.projectiles.add(sword);
                }
                index++;
            }
        }
        else if(Timer >= 27){
            int index = Timer-27;
            if(index<this.projectiles.size()){
                this.getCommandSenderWorld().addFreshEntity(this.projectiles.get(index));
            }
            else{
                this.projectiles.clear();
                this.addMorale(-0.3);
                this.setSkillDelay();
                return true;
            }
        }

        return false;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 3;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        target.hurt(this.isAngry()? DamageSources.causeRevengeDamage(this): DamageSource.mobAttack(this), damage+3);
        this.AttackCount = 0;
        if(MathUtil.rollBooleanRNG(0.25F)) {
            this.playSound(registerSounds.TEXAS_TALK_ATTACK, this.getSoundVolume(), this.getVoicePitch());
        }
        target.playSound(registerSounds.TEXAS_SWORD_HIT, 1, 0.8F+(0.4F*this.getRandom().nextFloat()));
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.VANGUARD;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.TexasMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.TexasSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.TexasHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.TexasAttackDamage.get())
                ;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return registerSounds.TEXAS_TALK_NORMAL;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return registerSounds.TEXAS_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return registerSounds.TEXAS_TALK_HIGH_AFFECTION2;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return registerSounds.TEXAS_TALK_HIGH_AFFECTION3;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.TEXAS_TALK_PAT;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.TEXAS_TALK_AGGRO;
    }
}
