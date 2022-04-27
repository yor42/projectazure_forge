package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityNagato extends EntityKansenBattleship implements IAzurLaneKansen {
    public EntityNagato(EntityType<? extends TamableAnimal> type, Level worldIn) {
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
                event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_loop"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleeping_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOpeningDoor()){
            if(this.getItemBySlot(EquipmentSlot.OFFHAND)== ItemStack.EMPTY && this.getItemBySlot(EquipmentSlot.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("openDoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("openDoorR", false));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof ItemGunBase){
            if(((ItemGunBase) this.getMainHandItem().getItem()).isTwoHanded()){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            event.getController().setAnimation(builder.addAnimation(this.swingingArm == InteractionHand.MAIN_HAND?"swingR":"swingL"));
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
        else if(this.isOrderedToSit() || this.getVehicle() != null) {
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_start_arm").addAnimation("idle_sit", true));
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

        if (!(this.animationSpeed > -0.15F && this.animationSpeed < 0.15F)) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("sail_arm", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walking_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else {
            if(this.getMainHandItem().getItem() instanceof ItemGunBase){
                if(((ItemGunBase) this.getMainHandItem().getItem()).isTwoHanded()){
                    event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
                }
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(builder.addAnimation("idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
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
                event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_loop"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            return PlayState.STOP;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_start").addAnimation("sit", true));
                }
            }
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("sail", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walking_leg", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public SoundEvent getDisappointedAmbientSound() {
        return registerSounds.NAGATO_TALK_DISAPPOINTED;
    }

    @Override
    public SoundEvent getStrangerAmbientSound() {
        return registerSounds.NAGATO_TALK_STRANGER;
    }

    @Override
    public SoundEvent getFriendlyAmbientSound() {
        return registerSounds.NAGATO_TALK_FRIENDLY;
    }

    @Override
    public SoundEvent getLikeAmbientSound() {
        return registerSounds.NAGATO_TALK_CRUSH;
    }

    @Override
    public SoundEvent getLoveAmbientSound() {
        return registerSounds.NAGATO_TALK_LOVE;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.NAGATO_TALK_ATTACK;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.NAGATO_TALK_PAT;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.NagatoMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.NagatoSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.NagatoHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.NagatoAttackDamage.get())
                ;
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

}
