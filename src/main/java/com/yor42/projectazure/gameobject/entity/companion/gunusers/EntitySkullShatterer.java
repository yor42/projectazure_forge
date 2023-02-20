package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.items.GasMaskFilterItem;
import com.yor42.projectazure.gameobject.items.GasMaskItem;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;

import static net.minecraft.util.Hand.MAIN_HAND;

public class EntitySkullShatterer extends EntityGunUserBase implements IAknOp {

    protected static final DataParameter<Boolean> COSMETIC = EntityDataManager.defineId(EntitySkullShatterer.class, DataSerializers.BOOLEAN);

    @Override
    public void populateInitialEquipments() {
        ItemStack gasmask = new ItemStack(RegisterItems.GASMASK.get());
        Item gasmaskitem = gasmask.getItem();

        if(gasmaskitem instanceof GasMaskItem){
            ListNBT filters = new ListNBT();
            for(int i=0; i<2; i++) {
                ItemStack filter = new ItemStack(RegisterItems.GASMASK_FILTER.get());
                ItemStackUtils.setCurrentDamage(filter, (int) (((GasMaskFilterItem) filter.getItem()).getMaxHP()* MathUtil.rand.nextFloat()));
                filters.add(filter.save(new CompoundNBT()));
            }
            gasmask.getOrCreateTag().put("filters", filters);
        }
        this.setItemSlot(EquipmentSlotType.HEAD, gasmask);
    }

    public EntitySkullShatterer(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COSMETIC, true);
    }

    public void SetCosmetic(boolean value){
        this.entityData.set(COSMETIC, value);
    }

    public boolean getCosmeticValue(){
        return this.entityData.get(COSMETIC);
    }

    @Nonnull
    @Override
    public ActionResultType interactAt(@Nonnull PlayerEntity player, @Nonnull Vector3d vec, @Nonnull Hand hand) {

        if(this.isOwnedBy(player)&&!this.getCommandSenderWorld().isClientSide() && player.getItemInHand(hand).isEmpty() && player.isCrouching()&&this.distanceTo(player)<=2){
            Vector3d PlayerLook = player.getViewVector(1.0F).normalize();
            float eyeHeight = (float) this.getEyeY();


            Vector3d EyeDelta = new Vector3d(this.getX() - player.getX(), eyeHeight - player.getEyeY(), this.getZ() - player.getZ());
            EyeDelta = EyeDelta.normalize();
            double EyeCheckFinal = PlayerLook.dot(EyeDelta);

            if (EyeCheckFinal > 0.998 && this.entityData.get(ECCI_ANIMATION_TIME)==0) {
                this.SetCosmetic(!this.getCosmeticValue());
                return ActionResultType.SUCCESS;
            }
        }
        return super.interactAt(player, vec, hand);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.SetCosmetic(compound.getBoolean("cosmetic"));
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("cosmetic", this.getCosmeticValue());
    }


    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.REUNION;
    }

    @Override
    public enums.GunClass getGunSpecialty() {
        return enums.GunClass.SMG;
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
            //event.getController().setAnimation(builder.addAnimation(this.swingingArm == Hand.MAIN_HAND?"swingL":"swingR", ILoopType.EDefaultLoopTypes.LOOP));

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
        else if(this.isEating()){
            if(this.getUsedItemHand() == MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.getUsedItemHand() == Hand.OFF_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_offhand", ILoopType.EDefaultLoopTypes.LOOP));
            }

            return PlayState.CONTINUE;
        }
        else if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
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
            if(this.getVehicle() != null && this.getVehicle() == this.getOwner()){
                event.getController().setAnimation(builder.addAnimation("carry_arm"));
                return PlayState.CONTINUE;
            }
            else if(this.isCriticallyInjured()){
                event.getController().setAnimation(builder.addAnimation("sit_injured_arm").addAnimation("sit_injured_arm_idle"));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("sit_arm_idle", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.isReloadingMainHand()) {
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
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", ILoopType.EDefaultLoopTypes.LOOP));

            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", ILoopType.EDefaultLoopTypes.LOOP));
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
            event.getController().setAnimation(builder.addAnimation("sleep_leg", ILoopType.EDefaultLoopTypes.LOOP));
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

    @Override
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this), buf -> buf.writeInt(this.getId()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.SPECIAL;
    }

    @Override
    public enums.OperatorClass getOperatorClass() {
        return enums.OperatorClass.SNIPER;
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

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.SkullShattererMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.SkullShattererSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.SkullShattererHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.SkullShattererAttackDamage.get())
                .add(Attributes.ARMOR, 15)
                ;
    }
}
