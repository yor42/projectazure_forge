package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IFGOServant;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.libs.enums;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;

import static com.yor42.projectazure.setup.register.RegisterItems.GAE_BOLG;

public class EntityScathath extends AbstractEntityCompanion implements IMeleeAttacker, IFGOServant {
    public EntityScathath(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.SERVANT;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {
        AnimationBuilder builder = new AnimationBuilder();
        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("faint_arm_idle", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            //event.getController().setAnimation(builder.addAnimation(this.swingingArm == Hand.MAIN_HAND?"swingL":"swingR", true));

            return PlayState.STOP;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd_chest", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE));

            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == InteractionHand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.getUsedItemHand() == InteractionHand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", ILoopType.EDefaultLoopTypes.LOOP));
            }

            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }
        if(this.isOrderedToSit()|| this.getVehicle() != null){
            if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("sit_arm_idle", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            return PlayState.STOP;
        }
        else if (isMoving()&& this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_leg"));
            return PlayState.CONTINUE;
        }else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_idle"));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_leg_idle"));
                }
            return PlayState.CONTINUE;
        }
        if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (isMoving() && this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 37;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Collections.singletonList(19));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(GAE_BOLG.get()));
    }

    @Override
    public boolean hasMeleeItem() {
        return false;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 5;
    }


    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if(this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(LivingEntity::isDeadOrDying).orElse(true) && this.getMainHandItem().getItem() == GAE_BOLG.get()){
            int swapindex = this.getItemSwapIndex(InteractionHand.MAIN_HAND);
            if(swapindex>-1){
                this.setItemInHand(InteractionHand.MAIN_HAND, this.getInventory().extractItem(swapindex, this.getInventory().getStackInSlot(swapindex).getCount(), false));
                this.setItemSwapIndexMainHand(-1);
            }
            else{
                this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean shouldUseNonVanillaAttack(LivingEntity target) {
        ItemStack MainHandItem = this.getMainHandItem();

        if(this.getTalentedWeaponList().contains(MainHandItem.getItem())){
            return true;
        }

        this.SwitchItem();
        return false;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        target.hurt(DamageSource.mobAttack(this), damage*4);
        target.knockback(0.12F, Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float)Math.PI / 180F)));
    }

    @Override
    public enums.SERVANT_CLASSES getServantClass() {
        return enums.SERVANT_CLASSES.LANCER;
    }

    @Nonnull
    @Override
    public ItemStack createWeaponStack() {
        return new ItemStack(GAE_BOLG.get());
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ScathathMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ScathathSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.ScathathHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ScathathAttackDamage.get())
                .add(Attributes.ARMOR, 4F)
                ;
    }

    @Override
    public SoundEvent getBondlevel1Sound() {
        return null;
    }

    @Override
    public SoundEvent getBondlevel2Sound() {
        return null;
    }
    @Override
    public SoundEvent getBondlevel3Sound() {
        return null;
    }
    @Override
    public SoundEvent getBondlevel4Sound() {
        return null;
    }
    @Override
    public SoundEvent getBondlevel5Sound() {
        return null;
    }

    @Override
    public SoundEvent getNormalAmbientSound() {
        return null;
    }
}
