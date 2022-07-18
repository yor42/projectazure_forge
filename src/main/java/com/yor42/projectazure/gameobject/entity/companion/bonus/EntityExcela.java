package com.yor42.projectazure.gameobject.entity.companion.bonus;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerShiningResonanceInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCausalBlackhole;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.interfaces.ISpellUser;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.TieredItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

import static com.yor42.projectazure.libs.enums.EntityType.SHININGRESONANCE;

public class EntityExcela extends AbstractEntityCompanion implements ISpellUser, IMeleeAttacker {

    public EntityExcela(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return SHININGRESONANCE;
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
            return PlayState.STOP;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd_chest", true));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", true));
            }
            else if(this.getUsedItemHand() == Hand.OFF_HAND){
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
        else if(this.isReloadingMainHand()) {
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_reload_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_reload_onehanded", true));
            }
            return PlayState.CONTINUE;
        }else if(this.isUsingGun()){
            if (((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
                event.getController().setAnimation(builder.addAnimation("gun_shoot_twohanded"));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_shoot_onehanded", true));
            }

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
        else if(this.isOrderedToSit()){
            event.getController().setAnimation(builder.addAnimation("sit_arm_idle", true));
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_onehanded", true));
            }
        }else if (this.isTalentedWeaponinMainHand()) {
            event.getController().setAnimation(builder.addAnimation("idle_lancer", true));
        }
        else if(this.isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
        }
        else {
            if (this.isTalentedWeaponinMainHand()) {
                event.getController().setAnimation(builder.addAnimation("idle_lancer", true));
            } else {
                event.getController().setAnimation(builder.addAnimation("idle_arm", true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public int SpellCooldown() {
        return 1800;
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
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_leg_idle", true));
                }
            }
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_leg", true));
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_leg", true));
        }
        else if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_leg", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_leg", true));
            }
        }
        else {
            event.getController().setAnimation(builder.addAnimation("idle_leg", true));
        }
        return PlayState.CONTINUE;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return registerSounds.EXCELA_TALK;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    protected float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerShiningResonanceInventory.Supplier(this), (buffer)->{buffer.writeInt(this.getId());});
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

    @Override
    protected boolean canEquipArmor() {
        return false;
    }

    @Override
    public int getInitialSpellDelay() {
        return 38;
    }

    @Override
    public int getProjectilePreAnimationDelay() {
        return 14;
    }

    @Override
    public Hand getSpellUsingHand() {
        return Hand.MAIN_HAND;
    }

    @Override
    public boolean shouldUseSpell(LivingEntity Target) {
        if(this.getGunStack().getItem() instanceof GunItem) {
            boolean hasAmmo = this.getGunStack().getOrCreateTag().getInt("AmmoCount") > 0;
            boolean reloadable = this.HasRightMagazine(this.getGunStack());

            return !(hasAmmo || reloadable);
        }

        boolean flag = !this.isSwimming() && !this.isOrderedToSit() && this.getVehicle() == null && this.RangedAttackCoolDown<=0;

        return flag;

    }

    @Override
    public void ShootProjectile(World world, @Nonnull LivingEntity target) {
        if(!world.isClientSide()) {
            EntityCausalBlackhole.SpawnAroundTarget((ServerWorld) world, this, target);
        }
        this.addMorale(-0.2);
        this.addExhaustion(0.3F);
    }

    @Override
    public void StartShootingEntityUsingSpell(LivingEntity target) {
        this.setSpellDelay(this.getInitialSpellDelay());
        this.StartedSpellAttackTimeStamp = this.tickCount;
        this.playSound(registerSounds.EXCELA_SKILL, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 18;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Collections.singletonList(8));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(registerItems.GRAVINET.get()));
    }

    @Override
    public boolean hasMeleeItem() {
        return this.isTalentedWeaponinMainHand();
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 4;
    }


    @Override
    public boolean shouldUseNonVanillaAttack(LivingEntity target) {
        if(target == null){
            return false;
        }
        boolean flg = this.hasMeleeItem() && !this.isSwimming() && !this.isOrderedToSit() && this.getVehicle() == null && !this.shouldUseSpell(target) && this.distanceTo(target) <=16;
        return flg;
    }

    @Override
    public void PerformMeleeAttack(LivingEntity target, float damage, int AttackCount) {
        if(target.isAlive()){
            if(this.isAngry() && target == this.getOwner()){
                target.hurt(DamageSources.causeRevengeDamage(this), this.getAttackDamageMainHand());
            }
            else{
                target.hurt(new EntityDamageSource("mob", this).bypassArmor(), this.getAttackDamageMainHand());
            }
        }
        this.playSound(registerSounds.EXCELA_ATTACK, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    public void StartMeleeAttackingEntity() {
        this.setMeleeAttackDelay((int) (this.MeleeAttackAnimationLength() *this.getAttackSpeedModifier(this.isTalentedWeaponinMainHand())));
        this.StartedMeleeAttackTimeStamp = this.tickCount;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ExcelaMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ExcelaSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.ExcelaHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ExcelaAttackDamage.get())
                .add(Attributes.ARMOR, PAConfig.CONFIG.ExcelaArmor.get())
                ;
    }
}
