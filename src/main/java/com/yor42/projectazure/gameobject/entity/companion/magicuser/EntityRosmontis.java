package com.yor42.projectazure.gameobject.entity.companion.magicuser;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.items.tools.ItemClaymore;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class EntityRosmontis extends AbstractCompanionMagicUser implements IAknOp {

    public EntityRosmontis(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.OPERATOR;
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
                event.getController().setAnimation(builder.addAnimation("faint_arm").addAnimation("faint_arm_loop", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            event.getController().setAnimation(builder.addAnimation(this.swingingArm == InteractionHand.MAIN_HAND?"swingR":"swingL", true));

            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", true));
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_arm", false));

            return PlayState.CONTINUE;
        }
        else if(this.getVehicle() == this.getOwner()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
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
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));

            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", true));

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
                event.getController().setAnimation(builder.addAnimation("sit_arm").addAnimation("sit_arm_idle", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isOpeningDoor()){
            if(this.getItemBySlot(EquipmentSlot.OFFHAND)== ItemStack.EMPTY && this.getItemBySlot(EquipmentSlot.MAINHAND) != ItemStack.EMPTY){
                event.getController().setAnimation(builder.addAnimation("openDoorL", false));
            }
            else{
                event.getController().setAnimation(builder.addAnimation("openDoorR", false));
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
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", true));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof ItemGunBase){
            if(((ItemGunBase) this.getMainHandItem().getItem()).isTwoHanded()){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", true));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        int skillitemcount = 0;
        for(int i=0;i<this.getSkillItemCount(); i++){
            if(this.isSkillItemInindex(i)){
                skillitemcount++;
            }
        }

        amount = amount - ((amount*0.75F)*((float)skillitemcount/(float)this.getSkillItemCount()));

        return super.hurt(source, amount);
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();

        if(this.isDeadOrDying()){
            if(this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("faint_leg").addAnimation("faint_leg_loop", true));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_leg", true));

            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_leg"));
            }
            else {
                if (this.isCriticallyInjured()) {
                    event.getController().setAnimation(builder.addAnimation("sit_injured_leg", false).addAnimation("sit_injured_leg_idle", true));
                } else {
                    event.getController().setAnimation(builder.addAnimation("sit").addAnimation("sit_leg_idle"));
                }
            }
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("ranged_attack_leg", false));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving()) {
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
    public ItemStack getItemInHand(InteractionHand p_184586_1_) {
        return super.getItemInHand(p_184586_1_);
    }

    @Override
    public boolean isSkillItem(ItemStack stack) {
        return stack.getItem() instanceof ItemClaymore;
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
    public int getInitialSpellDelay() {
        return 20;
    }

    @Override
    public boolean shouldUseSpell() {
        if(getGunStack().getItem() instanceof ItemGunBase) {
            boolean hasAmmo = getRemainingAmmo(getGunStack()) > 0;
            boolean reloadable = HasRightMagazine((((ItemGunBase) getGunStack().getItem()).getAmmoType()));

            return !(hasAmmo || reloadable) && !this.getNextSkillItem().isEmpty();
        }
        else return getItemInHand(getSpellUsingHand()).isEmpty() && !isSwimming()&& !this.getNextSkillItem().isEmpty();
    }

    @Override
    public int getSkillItemCount() {
        return 4;
    }

    @Override
    public int getProjectilePreAnimationDelay() {
        return 10;
    }

    @Override
    public InteractionHand getSpellUsingHand() {
        return InteractionHand.OFF_HAND;
    }

    @Override
    public void ShootProjectile(Level world, @Nonnull LivingEntity target) {
        EntityClaymore claymore = new EntityClaymore(Main.CLAYMORE.get(), world);
        claymore.setPos(target.getX(), target.getY(), target.getZ());
        claymore.setOwnerId(this.getUUID());
        world.addFreshEntity(claymore);
        int idx = this.getNextSkillItemindex();
        this.getInventory().extractItem(12+idx, 1, false);
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.SNIPER;
    }

    @Override
    public SoundEvent getNormalAmbientSounds() {
        return registerSounds.ROSMONTIS_TALK_NORMAL;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.RosmontisMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.RosmontisSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.RosmontisHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.RosmontisAttackDamage.get())
                ;
    }

    @Override
    public SoundEvent getAffection1AmbientSounds() {
        return registerSounds.ROSMONTIS_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getAffection2AmbientSounds() {
        return registerSounds.ROSMONTIS_TALK_HIGH_AFFECTION2;
    }

    @Override
    public SoundEvent getAffection3AmbientSounds() {
        return registerSounds.ROSMONTIS_TALK_HIGH_AFFECTION3;
    }

    @Nullable
    @Override
    public SoundEvent getPatSoundEvent() {
        return registerSounds.ROSMONTIS_TALK_PAT;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.ROSMONTIS_TALK_ATTACK;
    }
}
