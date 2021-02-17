package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
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

    public EntityAyanami(EntityType<? extends EntityAyanami> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4.0F);
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller_ayanami", 5, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {

        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        //event.getController().setAnimation(builder.addAnimation("animation.ayanami.idle", true));
        if(this.isBeingPatted()){
            if(this.isEntitySleeping())
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.pat_sit", true));
            else
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.pat", true));
            return PlayState.CONTINUE;
        }

        if(this.isEntitySleeping() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("animation.ayanami.sit1", true));
            return PlayState.CONTINUE;
        }

        if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.openDoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.openDoorR", false));
            }
        }
        else if(this.isMeleeing()){
            if(this.swingingHand == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("animation.ayanami.melee1", false));
            }
        }

        if (!(this.limbSwingAmount > -0.15F && this.limbSwingAmount < 0.15F)) {
            if(this.Sailing()){
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

        return PlayState.STOP;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35F)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 4.0F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0F)
                ;
    }

    @Override
    public int getRiggingOffset() {
        return 0;
    }

    @Override
    public enums.ShipRarity getRarity() {
        return enums.ShipRarity.ELITE;
    }
}
