package com.yor42.projectazure.gameobject.entity.companion.bonus;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.ISpellUser;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;

import static com.yor42.projectazure.libs.enums.EntityType.REUNION;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class EntityTalulah extends AbstractEntityCompanion implements IAknOp, IMeleeAttacker, ISpellUser {
    public EntityTalulah(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return REUNION;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> event) {
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
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("rangedattack_arm", true));
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
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));
            return PlayState.CONTINUE;
        }

        if(this.isOpeningDoor()){
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

        if(this.isOrderedToSit()){
            if(this.getMainHandItem().getItem() instanceof TieredItem){
                event.getController().setAnimation(builder.addAnimation("sit_idle_arm_toolmainhand", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_idle_arm_emptymainhand", true));
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

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent) {
        return PlayState.STOP;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
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
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_idle_leg", true));
                }
            }
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_leg", true));
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
        event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected void openGUI(ServerPlayer player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this), buf -> buf.writeInt(this.getId()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.SPECIAL;
    }

    @Override
    public int getInitialMeleeAttackDelay() {
        return 20;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Collections.singletonList(18));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return null;
    }

    @Override
    public boolean hasMeleeItem() {
        return this.getMainHandItem().getItem() instanceof SwordItem;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 3;
    }

    @Override
    public ItemStack getMainHandItem() {
        return super.getMainHandItem();
    }

    @Override
    public ItemStack getOffhandItem() {
        return super.getOffhandItem();
    }

    @Override
    public boolean shouldUseNonVanillaAttack(LivingEntity target) {
        if(target == null){
            return false;
        }
        return this.hasMeleeItem() && !this.isSwimming() && !this.isOrderedToSit() && this.getVehicle() == null && this.distanceTo(target) <=this.getAttackRange(this.isTalentedWeaponinMainHand());
    }

    @Override
    public boolean isTalentedWeapon(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        if(target.isAlive()){
            if(this.isAngry() && target == this.getOwner()){
                target.hurt(DamageSources.causeRevengeDamage(this), this.getAttackDamageMainHand());
            }
            else{
                target.hurt(DamageSource.mobAttack(this), this.getAttackDamageMainHand());
            }
        }
    }

    @Override
    public void StartMeleeAttackingEntity() {
        this.setMeleeAttackDelay((int) (this.getInitialMeleeAttackDelay() *this.getAttackSpeedModifier(this.isTalentedWeaponinMainHand())));
        this.StartedMeleeAttackTimeStamp = this.tickCount;
    }

    @Override
    public int getInitialSpellDelay() {
        return 20;
    }

    @Override
    public int getProjectilePreAnimationDelay() {
        return 14;
    }

    @Override
    public InteractionHand getSpellUsingHand() {
        return InteractionHand.OFF_HAND;
    }

    @Override
    public boolean shouldUseSpell() {
        if(this.getGunStack().getItem() instanceof ItemGunBase) {
            boolean hasAmmo = getRemainingAmmo(this.getGunStack()) > 0;
            boolean reloadable = this.HasRightMagazine((((ItemGunBase) this.getGunStack().getItem()).getAmmoType()));

            return !(hasAmmo || reloadable);
        }
        else return this.getItemInHand(getSpellUsingHand()).isEmpty() && !this.isSwimming() && !this.shouldUseNonVanillaAttack(this.getTarget());
    }

    @Override
    public void ShootProjectile(Level world, @Nonnull LivingEntity target) {
        if(target.isAlive()){
            target.hurt(DamageSources.causeArtsFireDamage(this), 6);
            target.setSecondsOnFire(5);
            this.addExp(0.2F);
            this.addExhaustion(0.05F);
            this.addMorale(-0.2);
        }
    }

    @Override
    public void StartShootingEntityUsingSpell(LivingEntity target) {
        this.setSpellDelay(this.getInitialSpellDelay());
        this.StartedSpellAttackTimeStamp = this.tickCount;
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.REUNION;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return null;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return null;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return null;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return null;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.TalulahMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.TalulahSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.TalulahHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.TalulahAttackDamage.get())
                ;
    }
}
