package com.yor42.projectazure.gameobject.entity.companion.sworduser;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EntityMudrock extends AbstractSwordUserBase implements IAknOp {
    public EntityMudrock(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

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
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            event.getController().setAnimation(builder.addAnimation(this.swingingArm == InteractionHand.MAIN_HAND?"swingR":"swingL"));
            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));
            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == InteractionHand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", true));
            }
            else if(this.getUsedItemHand() == InteractionHand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", true));
            }

            return PlayState.CONTINUE;
        }
        else if(this.isOpeningDoor()){
            if(this.getItemBySlot(EquipmentSlot.OFFHAND)== ItemStack.EMPTY && this.getItemBySlot(EquipmentSlot.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("opendoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("opendoorR", false));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isReloadingMainHand()){
            event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
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
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_arm", true));
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
                if (this.getMainHandItem().getItem() instanceof TieredItem) {
                    event.getController().setAnimation(builder.addAnimation("sit_idle_arm_toolmainhand", true));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_idle_arm_emptymainhand", true));
                }
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
            if(this.getMainHandItem().getItem() instanceof ItemGunBase){
                if(((ItemGunBase) this.getMainHandItem().getItem()).isTwoHanded()){
                    event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
                }
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(builder.addAnimation("idle_arm", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent) {
        return PlayState.STOP;
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
            }else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_idle_leg", true));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_leg", true));
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
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.CONTINUE;
    }
    @Override
    protected void openGUI(ServerPlayer player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this), buf -> buf.writeInt(this.getId()));
    }

    @Override
    protected void soundListener(SoundKeyframeEvent event) {
        this.playSound(registerSounds.HAMMER_SWING, 1, 0.8F+(0.4F*this.getRandom().nextFloat()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_6;
    }

    @Override
    public int getInitialMeleeAttackDelay() {
        return 20;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Arrays.asList(18));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(Main.SLEDGEHAMMER.get()));
    }

    @Override
    public ArrayList<Integer> getMeleeAnimationAudioCueDelay() {
        return new ArrayList<>(Collections.singletonList(12));
    }

    @Override
    public void playMeleeAttackPreSound() {
        this.playSound(registerSounds.HAMMER_SWING, 1, 0.8F+(0.2F*this.getRandom().nextFloat()));
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 3;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        //this.playSound(getAttackSound(), 1F, 0.8F + this.getRNG().nextFloat() * 0.4F);
        target.hurt(this.isAngry()? DamageSources.causeRevengeDamage(this): DamageSource.mobAttack(this), damage*0.5F);
        target.knockback(0.15F, Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float)Math.PI / 180F)));
        this.playSound(registerSounds.HAMMER_HIT, 1, 0.8F+(0.4F*this.getRandom().nextFloat()));
    }

    @Override
    public float getAttackSpeedModifier(boolean isUsingTalentedWeapon) {
        return isUsingTalentedWeapon? 1:1.2F;
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.DEFENDER;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return registerSounds.MUDROCK_TALK_NORMAL;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return registerSounds.MUDROCK_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return registerSounds.MUDROCK_TALK_HIGH_AFFECTION2;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return registerSounds.MUDROCK_TALK_HIGH_AFFECTION3;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.MUDROCK_TALK_ATTACK;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.MUDROCK_TALK_PAT;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.MudrockMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.MudrockSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.MudrockHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.MudrockAttackDamage.get())
                ;
    }
}
