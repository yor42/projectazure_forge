package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityAyanami extends EntityKansenDestroyer implements IAnimatable, IAzurLaneKansen {

    public EntityAyanami(EntityType<? extends EntityAyanami> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        AnimationBuilder builder = new AnimationBuilder();
        if(this.swinging){
            event.getController().setAnimation(builder.addAnimation(this.swingingArm == Hand.MAIN_HAND?"swingR":"swingL", true));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.sleep_upper_body", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", true));
            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.pat", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOpeningDoor()){
            if(this.getItemBySlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemBySlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.openDoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.openDoorR", false));
            }
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
        else if(this.isReloadingMainHand()){
            event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.swim_arm", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.sail_arm", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof ItemGunBase){
            if(((ItemGunBase) this.getMainHandItem().getItem()).isTwoHanded()){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("animation.ayanami.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent) {
        return PlayState.CONTINUE;
    }

    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event)
    {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.sleep_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.sit1Start").addAnimation("animation.ayanami.sit1", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.sail", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.run", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.walk", true));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("animation.ayanami.idle_leg", true));
        return PlayState.CONTINUE;
    }

    @Override
    public SoundEvent getDisappointedAmbientSound() {
        return registerSounds.AYANAMI_TALK_DISAPPOINTED;
    }

    @Override
    public SoundEvent getStrangerAmbientSound() {
        return registerSounds.AYANAMI_TALK_STRANGER;
    }

    @Override
    public SoundEvent getFriendlyAmbientSound() {
        return registerSounds.AYANAMI_TALK_FRIENDLY;
    }

    @Override
    public SoundEvent getLikeAmbientSound() {
        return registerSounds.AYANAMI_TALK_CRUSH;
    }

    @Override
    public SoundEvent getLoveAmbientSound() {
        return registerSounds.AYANAMI_TALK_LOVE;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.AYANAMI_TALK_ATTACK;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.AYANAMI_TALK_PAT;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.AyanamiMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.AyanamiSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.AyanamiHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.AyanamiAttackDamage.get())
                ;
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_4;
    }
}
