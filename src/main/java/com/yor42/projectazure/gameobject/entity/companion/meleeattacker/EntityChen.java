package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

import static com.yor42.projectazure.setup.register.registerSounds.*;

public class EntityChen extends AbstractSwordUserBase implements IAknOp {
    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
    }

    public EntityChen(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 31;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(24);
        return list;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.CHEN_TALK_AGGRO;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.CHEN_TALK_PAT;
    }

    public ArrayList<Item> getTalentedWeaponList(){
        return new ArrayList<>(Arrays.asList(registerItems.CHIXIAO.get(), registerItems.SHEATH.get()));
    }

    public SoundEvent getAttackSound(){
        return this.getMainHandItem().getItem() == registerItems.CHIXIAO.get()? CHIXIAO_HIT:SHEATH_HIT;
    }

    @Override
    public void awardKillScore(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_) {
        this.playSound(CHEN_TALK_KILL, this.getSoundVolume(), this.getVoicePitch());
        super.awardKillScore(p_191956_1_, p_191956_2_, p_191956_3_);
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return isUsingTalentedWeapon? 4:3;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        if(AttackCount == 1) {
            this.playSound(CHEN_TALK_ATTACK, this.getSoundVolume(), this.getVoicePitch());
        }
        boolean ishit = false;
        if(AttackCount == 2){
            ishit = target.hurt(this.isAngry()? DamageSources.causeRevengeDamage(this):DamageSource.mobAttack(this), damage+3);
            this.AttackCount = 0;
        }
        else{
            ishit = target.hurt(this.isAngry()? DamageSources.causeRevengeDamage(this):DamageSource.mobAttack(this), damage*0.5F);
            target.knockback(0.09F, MathHelper.sin(this.yRot * ((float)Math.PI / 180F)), -MathHelper.cos(this.yRot * ((float)Math.PI / 180F)));
        }

        if(ishit){
            target.playSound(getAttackSound(), 1F, 0.8F + this.getRandom().nextFloat() * 0.4F);
        }
    }

    @Override
    public float getAttackSpeedModifier(boolean isUsingTalentedWeapon) {
        return isUsingTalentedWeapon? 1:1.2F;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        super.registerControllers(animationData);
        animationData.addAnimationController(new AnimationController<>(this, "controller_tail", 10, this::predicate_tail));
    }

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
                    event.getController().setAnimation(builder.addAnimation("sit_leg_start").addAnimation("sit_leg", true));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_leg", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            return PlayState.STOP;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.STOP;
    }

    protected <E extends IAnimatable> PlayState predicate_tail(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOrderedToSit() && this.getVehicle() == null){
            event.getController().setAnimation(builder.addAnimation("sit_tail_start").addAnimation("sit_tail", true));
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_tail", true));
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat_tail", true));
        }
        else{
            event.getController().setAnimation(builder.addAnimation("idle_tail", true));
        }

        return PlayState.CONTINUE;

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
        if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));
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
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null) {
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                if(this.getMainHandItem().getItem() instanceof TieredItem){
                    event.getController().setAnimation(builder.addAnimation("sit_arm_toolmainhand", true));
                }
                else {
                    event.getController().setAnimation(builder.addAnimation("sit_arm_emptymainhand", true));
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
        else if(this.isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else{
            event.getController().setAnimation(builder.addAnimation("idle_arm", true));
            return PlayState.CONTINUE;
        }
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ChenMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ChenSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.ChenHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ChenAttackDamage.get())
                ;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this),buf -> buf.writeInt(this.getId()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_6;
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.GUARD;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return registerSounds.CHEN_TALK_NORMAL;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return registerSounds.CHEN_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return registerSounds.CHEN_TALK_HIGH_AFFECTION2;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return registerSounds.CHEN_TALK_HIGH_AFFECTION3;
    }

    @Override
    public boolean canUseSkill(LivingEntity target) {
        return super.canUseSkill(target) && this.hasSkillItem();
    }

    @Override
    public boolean isSkillItem(ItemStack stack) {
        return stack.getItem() == registerItems.CHIXIAO.get();
    }
}
