package com.yor42.projectazure.gameobject.entity.companion.kansen;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
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

public class EntityEnterprise extends EntityKansenAircraftCarrier implements IAzurLaneKansen {
    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSitting() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.sit_start").addAnimation("animation.enterprise.sit", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (!(this.limbSwingAmount > -0.1F && this.limbSwingAmount < 0.1F)) {
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
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.CONTINUE;
    }

    public EntityEnterprise(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        if(Minecraft.getInstance().isGamePaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.sleep", true));
            return PlayState.CONTINUE;
        }else if(this.isBeingPatted()){
            if(this.isSitting())
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat_sit", true));
            else
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.pat", true));
            return PlayState.CONTINUE;
        } else if(this.isOpeningDoor()){
            if(this.getItemStackFromSlot(EquipmentSlotType.OFFHAND)== ItemStack.EMPTY && this.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.doorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.doorR", false));
            }
            return PlayState.CONTINUE;
        }
        else if(this.ShouldPlayReloadAnim()){
            event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            return PlayState.CONTINUE;
        }
        else if(this.swingProgress>0){
            event.getController().setAnimation(builder.addAnimation(this.swingingHand == Hand.MAIN_HAND?"swingR":"swingL"));
            return PlayState.CONTINUE;
        }else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.enterprise.swim_arm", true));
            return PlayState.CONTINUE;
        }

        if (!(this.limbSwingAmount > -0.1F && this.limbSwingAmount < 0.1F)) {
            if(this.isSailing()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.sail_arm", true));
            }
            else if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("animation.enterprise.walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
            if(((ItemGunBase) this.getHeldItemMainhand().getItem()).isTwoHanded()){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("animation.enterprise.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public int getRiggingOffset() {
        return 0;
    }
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.func_233666_p_()
                //Attribute
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.EnterpriseMovementSpeed.get())
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.EnterpriseSwimSpeed.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, PAConfig.CONFIG.EnterpriseHealth.get())
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.EnterpriseAttackDamage.get())
                ;
    }
}
