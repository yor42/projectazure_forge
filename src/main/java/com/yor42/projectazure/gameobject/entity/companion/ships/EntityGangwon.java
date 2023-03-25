package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;

public class EntityGangwon extends EntityKansenDestroyer implements IAnimatable{

    public EntityGangwon(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_loop"));
            return PlayState.CONTINUE;
        }
        else if (this.isSleeping()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            return PlayState.STOP;
        }else if (this.entityData.get(ECCI_ANIMATION_TIME) > 0 && !this.isAngry()) {
            event.getController().setAnimation(builder.addAnimation("lewd", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (this.isBlocking()) {
            event.getController().setAnimation(builder.addAnimation("shield_block", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isBeingPatted()) {
            if (this.isOrderedToSit())
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.pat_sit", ILoopType.EDefaultLoopTypes.LOOP));
            else
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.pat", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isGettingHealed()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.heal_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }else if (this.isEating()) {
            if (this.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", ILoopType.EDefaultLoopTypes.LOOP));
            } else if (this.getUsedItemHand() == InteractionHand.OFF_HAND) {
                event.getController().setAnimation(builder.addAnimation("eat_offhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if (this.isOrderedToSit() && this.getVehicle()!= null) {
            if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.sit_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        
        else if(this.shouldPlayShipAttackAnim()){
            event.getController().setAnimation(builder.addAnimation("ship_fire"));
            return PlayState.CONTINUE;
        }
        else if (this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.swim_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (isMoving()) {
            if (this.isSailing()) {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.sail_arm", ILoopType.EDefaultLoopTypes.LOOP));
            } else if (this.isSprinting()) {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.run_arm", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.walk_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }else if(this.getMainHandItem().getItem() instanceof GunItem){
            return PlayState.STOP;
        }


        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent) {
        return PlayState.CONTINUE;
    }

    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event)
    {

        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_leg"));
            return PlayState.CONTINUE;
        }else if(this.isDeadOrDying()){
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_loop"));
                }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sleep", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sit_start").addAnimation("animation.gangwon.sit", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.sail", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.run", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.walk", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.GangwonMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.GangwonSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.GangwonHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.GangwonAttackDamage.get())
                ;
    }

    //Warship girls doesnt have rarity system. WHAT?
    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_4;
    }
}
