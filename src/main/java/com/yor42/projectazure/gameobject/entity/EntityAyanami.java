package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityAyanami extends EntityKansenDestroyer implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public EntityAyanami(EntityType<? extends EntityAyanami> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4.0F);
    }



    public boolean isWalking(AnimationEvent event){
        return !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        AnimationBuilder builder = new AnimationBuilder();
        if (!(this.limbSwingAmount > -0.15F && this.limbSwingAmount < 0.15F)) {
            if(this.canSail()){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.sail", true));
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.sail_hand", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.walk", true));
            }
            return PlayState.CONTINUE;
        }
        if(this.isEntitySleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.sit1", true));
        }
        else {
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3F)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 4.0F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0F)
                ;
    }
}
