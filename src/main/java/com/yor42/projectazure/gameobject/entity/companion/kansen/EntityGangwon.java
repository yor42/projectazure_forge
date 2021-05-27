package com.yor42.projectazure.gameobject.entity.companion.kansen;

import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class EntityGangwon extends EntityKansenDestroyer implements IAnimatable{

    public EntityGangwon(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sleep_arm", true));
            return PlayState.CONTINUE;
        }

        if(this.isBeingPatted()){
            if(this.isEntitySleeping())
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.pat_sit", true));
            else
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.pat", true));
            return PlayState.CONTINUE;
        } else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.heal_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.openDoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.openDoorR", false));
            }
        }
        else if(this.isMeleeing()){
            if(this.swingingHand == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("animation.gangwon.melee", false));
            }
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.swim_arm", true));
            return PlayState.CONTINUE;
        }else {

            if (!(this.limbSwingAmount > -0.15F && this.limbSwingAmount < 0.15F)) {
                if (this.isSailing()) {
                    event.getController().setAnimation(builder.addAnimation("animation.gangwon.sail_arm", true));
                } else if (this.isSprinting()) {
                    event.getController().setAnimation(builder.addAnimation("animation.gangwon.run_arm", true));
                } else {
                    event.getController().setAnimation(builder.addAnimation("animation.gangwon.walk_arm", true));
                }
                return PlayState.CONTINUE;
            }
            else{
                if(this.isEntitySleeping()) {
                    event.getController().setAnimation(builder.addAnimation("animation.gangwon.sit_arm", true));
                    return PlayState.CONTINUE;
                }
                else{
                    event.getController().setAnimation(builder.addAnimation("animation.gangwon.idle", true));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent) {
        return PlayState.CONTINUE;
    }

    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event)
    {

        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sleep", true));
            return PlayState.CONTINUE;
        }

        if(this.isEntitySleeping() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.sit", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.gangwon.swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (!(this.limbSwingAmount > -0.15F && this.limbSwingAmount < 0.15F)) {
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

        return PlayState.STOP;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35F)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 2.5F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }

    @Override
    public int getRiggingOffset() {
        return 28;
    }

    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.ELITE;
    }
}
