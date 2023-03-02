package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerGFLInventory;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityM4A1 extends EntityGunUserBase{

    public EntityM4A1(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.TDOLL;
    }

    @Override
    public enums.GunClass getGunSpecialty() {
        return enums.GunClass.AR;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }
        else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_idle"));
            return PlayState.CONTINUE;
        }
        if (this.isSleeping()) {
            event.getController().setAnimation(builder.addAnimation("sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.entityData.get(ECCI_ANIMATION_TIME) > 0 && !this.isAngry()) {
            event.getController().setAnimation(builder.addAnimation("lewd", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            return PlayState.STOP;
        }
        else if (this.isBeingPatted()) {
            event.getController().setAnimation(builder.addAnimation("pat", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isGettingHealed()) {
            event.getController().setAnimation(builder.addAnimation("heal", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if(this.isReloadingMainHand()) {
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_reload_onehanded", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_shoot_onehanded", ILoopType.EDefaultLoopTypes.LOOP));
            }

            return PlayState.CONTINUE;
        }
        else if (this.isEating()) {
            if (this.getUsedItemHand() == Hand.MAIN_HAND) {
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", ILoopType.EDefaultLoopTypes.LOOP));
            } else if (this.getUsedItemHand() == Hand.OFF_HAND) {
                event.getController().setAnimation(builder.addAnimation("eat_offhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        } else if (this.isBeingPatted()) {
            event.getController().setAnimation(builder.addAnimation("pat", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.getVehicle() != null && this.getVehicle() == this.getOwner()) {
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }else if (this.isGettingHealed()) {
            event.getController().setAnimation(builder.addAnimation("heal_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
            }
            else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_arm").addAnimation("sit_arm_idle", ILoopType.EDefaultLoopTypes.LOOP));
                }
            }
            return PlayState.CONTINUE;
        }
        else if (this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if (this.getMainHandItem().getItem() instanceof GunItem) {
                if (this.isSprinting()) {
                    event.getController().setAnimation(builder.addAnimation("gun_run_twohanded", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", ILoopType.EDefaultLoopTypes.LOOP));
                }
            } else {
                if (this.isSprinting()) {
                    event.getController().setAnimation(builder.addAnimation("run_arm", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(builder.addAnimation("walk_hand", ILoopType.EDefaultLoopTypes.LOOP));
                }
            }
            return PlayState.CONTINUE;
        }

        else if(this.getMainHandItem().getItem() instanceof GunItem){
            if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_onehanded", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();
        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_leg"));
            return PlayState.CONTINUE;
        }
        else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_idle"));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("sit_injured_idle", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit").addAnimation("sit_idle", ILoopType.EDefaultLoopTypes.LOOP));
                }
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("leg_idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.M4A1MovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.M4A1SwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.M4A1Health.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.M4A1AttackDamage.get())
                ;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return registerSounds.M4A1_TALK_NORMAL;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.M4A1_TALK_ATTACK;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.M4A1_TALK_PAT;
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_4;
    }

    @Override
    protected float getSitHeight() {
        return 1.35F;
    }

    @Override
    public void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerGFLInventory.Supplier(this),buf -> buf.writeInt(this.getId()));
    }
}
