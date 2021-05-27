package com.yor42.projectazure.gameobject.entity.companion.gunusers.bluearchive;

import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
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

public class EntityShiroko extends EntityGunUserBase {
    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.BLUEARCHIVE;
    }

    public EntityShiroko(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.pat", true));
            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.heal", true));
            return PlayState.CONTINUE;
        }
        else if(this.ShouldPlayReloadAnim()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.gun_shoot_twohanded_reload"));
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.gun_shoot_twohanded"));
            return PlayState.CONTINUE;
        }else if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("animation.shiroko.openDoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("animation.shiroko.openDoorR", false));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isMeleeing()){
            if(this.swingingHand == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("animation.shiroko.melee1", false));
            }
            return PlayState.CONTINUE;
        }else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.swim_arm", true));
            return PlayState.CONTINUE;
        }else{
            if (!(this.limbSwingAmount > -0.15F && this.limbSwingAmount < 0.15F)) {
                if(this.isSprinting()){
                    event.getController().setAnimation(builder.addAnimation("animation.shiroko.run_hand", true));
                }
                else {
                    event.getController().setAnimation(builder.addAnimation("animation.shiroko.walk_hand", true));
                }
                return PlayState.CONTINUE;
            }
            else{
                if(this.isEntitySleeping() || this.getRidingEntity() != null){
                    event.getController().setAnimation(builder.addAnimation("animation.shiroko.sit_hand", true));
                    return PlayState.CONTINUE;
                }
                if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){

                    if(((ItemGunBase) this.getHeldItemMainhand().getItem()).isTwoHanded()){
                        event.getController().setAnimation(builder.addAnimation("animation.shiroko.gun_idle_twohanded", true));
                    }
                    return PlayState.CONTINUE;
                }
            }
        }
        event.getController().setAnimation(builder.addAnimation("animation.shiroko.idle_arm", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {

        return PlayState.CONTINUE;
    }

    @Override
    public int Reload_Anim_Delay() {
        return 58;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.sleep", true));
            return PlayState.CONTINUE;
        }

        if(this.isEntitySleeping() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.sit", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (!(this.limbSwingAmount > -0.15F && this.limbSwingAmount < 0.15F)) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.shiroko.run", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.shiroko.walk", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("animation.shiroko.idle", true));
        return PlayState.CONTINUE;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4F)
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 2.5F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2F)
                ;
    }

}
