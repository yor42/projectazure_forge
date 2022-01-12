package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import com.yor42.projectazure.gameobject.items.ItemMissleDrone;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;

public class EntityShiroko extends EntityGunUserBase {
    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.BLUEARCHIVE;
    }

    public EntityShiroko(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.GunClass getGunSpecialty() {
        return enums.GunClass.AR;
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerBAInventory.Supplier(this));
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.pat", true));
            return PlayState.CONTINUE;
        }
        else if(this.dataManager.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.heal", true));
            return PlayState.CONTINUE;
        }
        else if(this.isReloadingMainHand()){
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
        else if(this.isEating()){
            if(this.getActiveHand() == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", true));
            }
            else if(this.getActiveHand() == Hand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSwingInProgress){
            event.getController().setAnimation(builder.addAnimation(this.swingingHand == Hand.MAIN_HAND?"swingR":"swingL"));
            return PlayState.CONTINUE;
        }
        else if(this.isActiveItemStackBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", true));
            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.swim_arm", true));
            return PlayState.CONTINUE;
        }else{
            if (isMoving()) {
                if(this.isSprinting()){
                    event.getController().setAnimation(builder.addAnimation("animation.shiroko.run_arm", true));
                }
                else {
                    event.getController().setAnimation(builder.addAnimation("animation.shiroko.walk_hand", true));
                }
                return PlayState.CONTINUE;
            }
            else{
                if(this.isSitting() || this.getRidingEntity() != null){
                    event.getController().setAnimation(builder.addAnimation("animation.shiroko.sit_hand", true));
                    return PlayState.CONTINUE;
                }
                if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){

                    if(((ItemGunBase) this.getHeldItemMainhand().getItem()).isTwoHanded()){
                        event.getController().setAnimation(builder.addAnimation("animation.shiroko.gun_idle_twohanded", true));
                    }else{
                        event.getController().setAnimation(builder.addAnimation("animation.shiroko.idle_arm", true));
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
        return 63;
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
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

        if(this.isSitting() || this.getRidingEntity() != null){
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.sit_start").addAnimation("animation.shiroko.sit", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("animation.shiroko.swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
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
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ShirokoMovementSpeed.get())
                .createMutableAttribute(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ShirokoSwimSpeed.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, PAConfig.CONFIG.ShirokoHealth.get())
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ShirokoAttackDamage.get())
                ;
    }

    @Override
    public boolean performOneTimeSkill(LivingEntity target) {

        ItemStack stack = this.getSkillItem(0);
        if(stack.getItem() instanceof ItemMissleDrone && this.getAttackTarget() != null){
            AbstractEntityDrone drone = ((ItemMissleDrone) stack.getItem()).CreateDrone(this.getEntityWorld(), stack, this);
            if(drone != null){
                drone.setAttackTarget(this.getAttackTarget());
                this.getEntityWorld().addEntity(drone);
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canUseSkill() {
        ItemStack droneStack = this.getInventory().getStackInSlot(12);
        if(droneStack.getItem() instanceof ItemMissleDrone) {
            boolean hasSkillItem = this.isSkillItem(droneStack);
            boolean isDroneReady = ItemStackUtils.getCurrentHP(droneStack) > 2;
            boolean isDroneArmed = ItemStackUtils.getRemainingAmmo(droneStack) > 0;
            return hasSkillItem && isDroneArmed && isDroneReady;
        }
        return false;
    }

    @Override
    public boolean isSkillItem(ItemStack stack) {
        return stack.getItem() instanceof ItemMissleDrone;
    }
}
