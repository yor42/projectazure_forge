package com.yor42.projectazure.gameobject.entity.companion.sworduser;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

import static com.yor42.projectazure.setup.register.registerSounds.CHIXIAO_HIT;
import static com.yor42.projectazure.setup.register.registerSounds.SHEATH_HIT;

public class EntityChen extends AbstractSwordUserBase implements IAknOp {
    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
    }

    public EntityChen(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public int getInitialMeleeAttackDelay() {
        return 31;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(24);
        return list;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.CHEN_TALK_ATTACK;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.CHEN_TALK_PAT;
    }

    public ArrayList<Item> getTalentedWeaponList(){
        return new ArrayList<>(Arrays.asList(registerItems.CHIXIAO.get(), registerItems.SHEATH.get()));
    }

    public SoundEvent getAttackSound(){
        return this.getMainHandItem().getItem() == registerItems.CHIXIAO.get()? CHIXIAO_HIT:SHEATH_HIT;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return isUsingTalentedWeapon? 4:3;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        this.playSound(getAttackSound(), 1F, 0.8F + this.getRandom().nextFloat() * 0.4F);
        if(AttackCount == 2){
            target.hurt(this.isAngry()? DamageSources.causeRevengeDamage(this): DamageSource.mobAttack(this), damage+3);
            this.AttackCount = 0;
        }
        else{
            target.hurt(this.isAngry()? DamageSources.causeRevengeDamage(this):DamageSource.mobAttack(this), damage*0.5F);
            target.knockback(0.09F, Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float)Math.PI / 180F)));
        }
    }

    @Override
    public float getAttackSpeedModifier(boolean isUsingTalentedWeapon) {
        return isUsingTalentedWeapon? 1:1.2F;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        super.registerControllers(animationData);
        animationData.addAnimationController(new AnimationController<>(this, "controller_tail", 10, this::predicate_tail));
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();
        if(this.isDeadOrDying()){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_idle"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg").addAnimation("sit_injured_leg_idle"));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit_leg_start").addAnimation("sit_leg", true));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_leg", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            return PlayState.STOP;
        }
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.STOP;
    }

    protected <E extends IAnimatable> PlayState predicate_tail(AnimationEvent<E> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOrderedToSit() && this.getVehicle() == null){
            event.getController().setAnimation(builder.addAnimation("sit_tail_start").addAnimation("sit_tail", true));
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_tail", true));
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat_tail", true));
        }
        else{
            event.getController().setAnimation(builder.addAnimation("idle_tail", true));
        }

        return PlayState.CONTINUE;

    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {

        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isDeadOrDying()){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_idle"));
            }
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            event.getController().setAnimation(builder.addAnimation(this.swingingArm == InteractionHand.MAIN_HAND?"swingR":"swingL"));
            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        }
        if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));
            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == InteractionHand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", true));
            }
            else if(this.getUsedItemHand() == InteractionHand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", true));
            }

            return PlayState.CONTINUE;
        }
        else if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }
        else if(this.isOpeningDoor()){
            if(this.getItemBySlot(EquipmentSlot.OFFHAND)== ItemStack.EMPTY && this.getItemBySlot(EquipmentSlot.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("opendoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("opendoorR", false));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isReloadingMainHand()){
            event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            return PlayState.CONTINUE;
        }
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", true));
            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", true));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null) {
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                if(this.getMainHandItem().getItem() instanceof TieredItem){
                    event.getController().setAnimation(builder.addAnimation("sit_arm_toolmainhand", true));
                }
                else {
                    event.getController().setAnimation(builder.addAnimation("sit_arm_emptymainhand", true));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
            return PlayState.CONTINUE;
        }

        else{
            if(this.getMainHandItem().getItem() instanceof ItemGunBase){
                if(((ItemGunBase) this.getMainHandItem().getItem()).isTwoHanded()){
                    event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
                }
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(builder.addAnimation("idle_arm", true));
            return PlayState.CONTINUE;
        }
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ChenMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ChenSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.ChenHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ChenAttackDamage.get())
                ;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    protected void openGUI(ServerPlayer player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this), buf -> buf.writeInt(this.getId()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_6;
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.GUARD;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return registerSounds.CHEN_TALK_NORMAL;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return registerSounds.CHEN_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return registerSounds.CHEN_TALK_HIGH_AFFECTION2;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return registerSounds.CHEN_TALK_HIGH_AFFECTION3;
    }

    @Override
    public boolean canUseSkill() {
        return this.hasSkillItem();
    }

    @Override
    public boolean isSkillItem(ItemStack stack) {
        return stack.getItem() == registerItems.CHIXIAO.get();
    }
}
