package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.TieredItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yor42.projectazure.libs.enums.CompanionRarity.STAR_5;
import static com.yor42.projectazure.setup.register.registerItems.WARHAMMER;

public class EntityNearl extends AbstractSwordUserBase implements IAknOp {
    public List<Entity> HealTarget = new ArrayList<>();

    public EntityNearl(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
    }

    public enums.OperatorClass getOperatorClass(){
        return enums.OperatorClass.DEFENDER;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return null;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return null;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return null;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return null;
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
        return 12;
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
        else if(this.swinging){
            return PlayState.STOP;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.getSkillAnimationTime()>0){
            event.getController().setAnimation(builder.addAnimation("skill_arm", false));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_arm", false));

            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", true));
            }
            else if(this.getUsedItemHand() == Hand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", true));
            }

            return PlayState.CONTINUE;
        }
        else if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));

            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", true));

            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit()|| this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                if (this.getMainHandItem().getItem() instanceof TieredItem) {
                    event.getController().setAnimation(builder.addAnimation("sit_idle_arm_toolmainhand", true));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_idle_arm_emptymainhand", true));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.isReloadingMainHand()) {
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_reload_onehanded", true));
            }
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_shoot_onehanded", true));
            }

            return PlayState.CONTINUE;
        }
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", true));

            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()&& this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_onehanded", true));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        event.getController().setAnimation(builder.addAnimation("idle_chest", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
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
            event.getController().setAnimation(builder.addAnimation("sleep_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){

            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_idle_leg"));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.getSkillAnimationTime()>0){
            event.getController().setAnimation(builder.addAnimation("skill_leg", false));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_leg", false));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving() && this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this), buf -> buf.writeInt(this.getId()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return STAR_5;
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 40;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Collections.singletonList(8));
    }

    @Override
    public ArrayList<Integer> getMeleeAnimationAudioCueDelay() {
        return new ArrayList<>(Collections.singletonList(12));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(WARHAMMER.get()));
    }

    @Override
    public Hand getNonVanillaMeleeAttackHand() {
        return Hand.MAIN_HAND;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 3;
    }

    public void UpdateandPerformNonVanillaMeleeAttack(){
        int currentspelldelay = this.getNonVanillaMeleeAttackDelay();
        @Nullable
        LivingEntity target = this.getTarget();
        if (currentspelldelay > 0) {
            this.getNavigation().stop();
            if (target == null || !target.isAlive()) {
                this.setMeleeAttackDelay(0);
                this.AttackCount = 0;
            }

            else {
                this.lookAt(target, 30.0F, 30.0F);
                this.setMeleeAttackDelay(currentspelldelay - 1);
                int delay = this.tickCount - this.StartedMeleeAttackTimeStamp;
                if (!this.getMeleeAnimationAudioCueDelay().isEmpty() && this.getMeleeAnimationAudioCueDelay().contains(delay)) {
                    this.playMeleeAttackPreSound();
                }
                if (this.getAttackDamageDelay().contains(delay) && this.distanceTo(target) <= ((IMeleeAttacker) this).getAttackRange(((IMeleeAttacker) this).isTalentedWeaponinMainHand())) {
                    this.AttackCount += 1;
                    this.PerformMeleeAttack(target, this.getAttackDamageMainHand(), this.AttackCount);
                }
            }
        } else if (this.AttackCount > 0) {
            this.AttackCount = 0;
        }

    }

    @Override
    public boolean canUseSkill(LivingEntity target) {
        int currentspelldelay = this.getNonVanillaMeleeAttackDelay();
        if(currentspelldelay == 0 && this.getSkillPoints()>=6){
            if((this.getHealth()/this.getMaxHealth())<=0.5F){
                return true;
            }
            else if(this.getSkillPoints()>=6 && this.getOwner()!=null){
                List<Entity> HealTarget = this.getCommandSenderWorld().getEntities(this, this.getBoundingBox().expandTowards(10, 2, 10), (entity) -> entity instanceof LivingEntity && (EntityNearl.this.isOwnedBy((LivingEntity) entity) || (entity instanceof TameableEntity && ((TameableEntity) entity).isOwnedBy(EntityNearl.this.getOwner()))) && ((((LivingEntity) entity).getHealth()/((LivingEntity) entity).getMaxHealth())<=0.5F));
                if(!HealTarget.isEmpty()){
                    this.HealTarget = HealTarget;
                    return true;
                };
            }
        }
        return false;
    }

    @Override
    public boolean performOneTimeSkill(LivingEntity target) {
        return this.HealTarget.isEmpty();
    }

    @Override
    public boolean performSkillTick(LivingEntity target, int Timer) {
        if(Timer == 0){
            this.setSkillAnimationTime(this.SkillAnimationLength());
        }
        else if(Timer == 20){
            if(!this.HealTarget.isEmpty()) {
                Entity entity2heal = HealTarget.get(0);
                if(this.getHealth()/this.getMaxHealth()<=0.5F){
                    entity2heal = this;
                }
                else {
                    for (Entity entity : HealTarget) {
                        if (entity2heal instanceof LivingEntity && entity instanceof LivingEntity) {
                            if (((LivingEntity) entity).getHealth() <= ((LivingEntity) entity2heal).getHealth()) {
                                entity2heal = entity;
                            }
                        }
                    }
                }

                if (entity2heal instanceof LivingEntity) {
                    ((LivingEntity) entity2heal).heal(Math.max(5, this.getAttackDamageMainHand()));
                    for(int i = 0; i < 5; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, entity2heal.getRandomX(1.0D), entity2heal.getRandomY() + 1.0D, entity2heal.getRandomZ(1.0D), d0, d1, d2);
                    }
                    this.addMorale(-1);
                    this.playSound(registerSounds.NEARL_HEAL, 0.8F+(this.random.nextFloat()*0.4F), 0.8F+(this.random.nextFloat()*0.4F));
                    this.getCommandSenderWorld().playSound(null, entity2heal.blockPosition(), registerSounds.HEAL_BOOST, SoundCategory.NEUTRAL, 0.8F+(this.random.nextFloat()*0.4F), 0.8F+(this.random.nextFloat()*0.4F));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void resetSkill() {
        super.resetSkill();
        this.HealTarget.clear();
    }

    public int SkillAnimationLength(){
        return 18;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        target.hurt(DamageSource.mobAttack(this), this.getAttackDamageMainHand());
        target.playSound(registerSounds.WARHAMMER_HIT, 1, 0.8F+(0.2F*this.getRandom().nextFloat()));
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.NearlMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.NearlSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.NearlHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.NearlAttackDamage.get())
                .add(Attributes.ARMOR, 8F)
                ;
    }
}
