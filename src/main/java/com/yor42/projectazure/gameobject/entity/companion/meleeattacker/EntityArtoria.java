package com.yor42.projectazure.gameobject.entity.companion.meleeattacker;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerFGOInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IFGOServant;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
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

import static net.minecraft.util.Hand.MAIN_HAND;

public class EntityArtoria extends AbstractEntityCompanion implements IMeleeAttacker, IFGOServant {

    private boolean gaveSheath;

    public EntityArtoria(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.gaveSheath = false;
    }

    @Override
    public int MeleeAttackAnimationLength() {
        return 36;
    }

    @Override
    public ArrayList<Integer> getAttackDamageDelay() {
        return new ArrayList<>(Collections.singletonList(22));
    }

    @Override
    public ArrayList<Item> getTalentedWeaponList() {
        return new ArrayList<>(Collections.singletonList(registerItems.EXCALIBUR.get()));
    }

    @Override
    public boolean hasMeleeItem() {
        //We don't need this lol
        return false;
    }

    @Override
    public float getAttackRange(boolean isUsingTalentedWeapon) {
        return 4;
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
        if(AttackCount == 1 && MathUtil.rollBooleanRNG(0.25F)){
            this.playSound(registerSounds.ARTORIA_TALK_ATTACK, this.getSoundVolume(), this.getVoicePitch());
        }
        target.hurt(DamageSource.mobAttack(this), damage*4);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if(!this.getCommandSenderWorld().isClientSide() && this.tickCount%200 == 0) {
            if (!this.gaveSheath && this.getAffection()>90){

                if(this.getOwner() instanceof PlayerEntity){
                    this.giveSheath((PlayerEntity) this.getOwner());
                }
            }
        }

        if(this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(LivingEntity::isDeadOrDying).orElse(true) && this.getMainHandItem().getItem() == registerItems.EXCALIBUR.get()){
            int swapindex = this.getItemSwapIndex(MAIN_HAND);
            if(swapindex>-1){
                this.setItemInHand(MAIN_HAND, this.getInventory().extractItem(swapindex, this.getInventory().getStackInSlot(swapindex).getCount(), false));
                this.setItemSwapIndexMainHand(-1);
            }
            else{
                this.setItemInHand(MAIN_HAND, ItemStack.EMPTY);
            }
        }
    }

    private void giveSheath(PlayerEntity player) {
        ItemStack stack = new ItemStack(registerItems.EXCALIBUR_SHEATH.get());
        stack.getOrCreateTag().putUUID("owner", this.getUUID());
        if(player.inventory.add(stack)){
            this.gaveSheath = true;
        }
    }

    @Override
    protected void onAffectionChange(float prevValue, float afterValue, PlayerEntity owner) {
        if(prevValue<90 && afterValue >=90){
            if(!this.gaveSheath) {
                this.giveSheath(owner);
            }
        }
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.SERVANT;
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
            //event.getController().setAnimation(builder.addAnimation(this.swingingArm == Hand.MAIN_HAND?"swingL":"swingR", true));

            return PlayState.STOP;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd_chest", true));
            return PlayState.CONTINUE;
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_arm", false));

            return PlayState.CONTINUE;
        }
        else if(this.isEating()){
            if(this.getUsedItemHand() == MAIN_HAND){
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
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", true));

            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", true));

            return PlayState.CONTINUE;
        }
        if(this.isOrderedToSit()|| this.getVehicle() != null){
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_arm_idle", true));
            }
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
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", true));
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_twohanded", true));
            }
            else if(((GunItem) this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof OneHandedPose){
                event.getController().setAnimation(builder.addAnimation("gun_idle_onehanded", true));
            }
            return PlayState.CONTINUE;
        }
        else if (isMoving()&& this.getVehicle() == null) {
            if(this.isSprinting()){
                event.getController().setAnimation(builder.addAnimation("run_arm", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("walk_arm", true));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(builder.addAnimation("idle_arm", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
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
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_leg_idle"));
                }
            }
            return PlayState.CONTINUE;
        }
        if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("melee_attack_leg", false));
            return PlayState.CONTINUE;
        }
        else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", true));
            return PlayState.CONTINUE;
        }

        if (isMoving() && this.getVehicle() == null) {
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

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        if(this.getRandom().nextFloat()<0.2F){
            return registerSounds.ARTORIA_TALK_HURT;
        }
        return super.getHurtSound(damageSourceIn);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return registerSounds.ARTORIA_TALK_DIE;
    }

    @Override
    public void awardKillScore(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_) {
        super.awardKillScore(p_191956_1_, p_191956_2_, p_191956_3_);
        this.playSound(registerSounds.ARTORIA_TALK_KILL, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerFGOInventory.Supplier(this), buf -> buf.writeInt(this.getId()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.STAR_5;
    }

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.ArtoriaMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.ArtoriaSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.ArtoriaHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.ArtoriaAttackDamage.get())
                .add(Attributes.ARMOR, 15F)
                ;
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        boolean gavesheath = compound.getBoolean("gavesheath");
        this.gaveSheath = gavesheath;
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT compound) {
        boolean gaveSheath = this.gaveSheath;
        compound.putBoolean("gavesheath", gaveSheath);
        super.addAdditionalSaveData(compound);
    }

    @Override
    public enums.SERVANT_CLASSES getServantClass() {
        return enums.SERVANT_CLASSES.SABER;
    }

    @Override
    public SoundEvent getNormalAmbientSound() {
        return registerSounds.ARTORIA_TALK_NORMAL;
    }

    @Override
    public SoundEvent getBondlevel1Sound() {
        return registerSounds.ARTORIA_TALK_HIGH_AFFECTION1;
    }

    @Override
    public SoundEvent getBondlevel2Sound() {
        return registerSounds.ARTORIA_TALK_HIGH_AFFECTION2;
    }

    @Override
    public SoundEvent getBondlevel3Sound() {
        return registerSounds.ARTORIA_TALK_HIGH_AFFECTION3;
    }

    @Override
    public SoundEvent getBondlevel4Sound() {
        return registerSounds.ARTORIA_TALK_HIGH_AFFECTION4;
    }

    @Override
    public SoundEvent getBondlevel5Sound() {
        return registerSounds.ARTORIA_TALK_HIGH_AFFECTION5;
    }

    @Nullable
    @Override
    protected SoundEvent getAggroedSoundEvent() {
        return registerSounds.ARTORIA_TALK_AGGRO;
    }

    @Nonnull
    @Override
    public ItemStack createWeaponStack() {
        ItemStack stack = new ItemStack(registerItems.EXCALIBUR.get());
        stack.getOrCreateTag().putUUID("owner", this.getUUID());
        return stack;
    }
}
