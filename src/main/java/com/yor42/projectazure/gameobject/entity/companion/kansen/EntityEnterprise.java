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

import static com.yor42.projectazure.libs.enums.CompanionRarity.SUPER_RARE;

public class EntityEnterprise extends EntityKansenAircraftCarrier{
    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.sleep", true));
            return PlayState.CONTINUE;
        }

        if(this.isBeingPatted()){
            if(this.isEntitySleeping())
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat_sit", true));
            else
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat", true));
            return PlayState.CONTINUE;
        }

        if(this.isEntitySleeping() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.sit", true));
            return PlayState.CONTINUE;
        }

        if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.doorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.doorR", false));
            }
        }
        else if(this.isMeleeing()){
            if(this.swingingHand == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.melee", false));
            }
        }

        if (!(this.limbSwingAmount > -0.15F && this.limbSwingAmount < 0.15F)) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.sail", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.run", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.walk", true));
            }
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    public EntityEnterprise(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public int getRiggingOffset() {
        return 0;
    }

    @Override
    public enums.CompanionRarity getRarity() {
        return SUPER_RARE;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35F)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 1.0F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }
}
