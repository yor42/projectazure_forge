package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
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

public class EntityGangwon extends EntityKansenDestroyer implements IAnimatable{

    public EntityGangwon(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        if (Minecraft.getInstance().isPaused()) {
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
        else if (this.isSleeping()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sleep_arm", true));
            return PlayState.CONTINUE;
        } else if (this.entityData.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME) > 0 && !this.isAngry()) {
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        } else if (this.isBlocking()) {
            event.getController().setAnimation(builder.addAnimation("shield_block", true));
            return PlayState.CONTINUE;
        } else if (this.isBeingPatted()) {
            if (this.isOrderedToSit())
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.pat_sit", true));
            else
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.pat", true));
            return PlayState.CONTINUE;
        } else if (this.isGettingHealed()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.heal_arm", true));
            return PlayState.CONTINUE;
        } else if (this.isOpeningDoor()) {
            if (this.getItemBySlot(EquipmentSlot.OFFHAND) == ItemStack.EMPTY && this.getItemBySlot(EquipmentSlot.MAINHAND) != ItemStack.EMPTY) {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.openDoorL", false));
            } else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.openDoorR", false));
            }
        } else if (this.isEating()) {
            if (this.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", true));
            } else if (this.getUsedItemHand() == InteractionHand.OFF_HAND) {
                event.getController().setAnimation(builder.addAnimation("eat_offhand", true));
            }
            return PlayState.CONTINUE;
        }
        else if (this.isOrderedToSit() && this.getVehicle()!= null) {
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.sit_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else if (this.isReloadingMainHand()) {
            event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            return PlayState.CONTINUE;
        } else if (this.isUsingGun()) {
            event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            return PlayState.CONTINUE;
        } else if (this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.swim_arm", true));
            return PlayState.CONTINUE;
        } else if (isMoving()) {
            if (this.isSailing()) {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.sail_arm", true));
            } else if (this.isSprinting()) {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.run_arm", true));
            } else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.walk_arm", true));
            }
            return PlayState.CONTINUE;
        }else {
            if (this.getMainHandItem().getItem() instanceof ItemGunBase) {
                if (((ItemGunBase) this.getMainHandItem().getItem()).isTwoHanded()) {
                    event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
                }
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.idle", true));
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

        if(this.isDeadOrDying()){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_loop"));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sleep", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.sit_start").addAnimation("animation.gangwon.sit", true));
            }
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.swim_leg", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.sail", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.run", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.walk", true));
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
