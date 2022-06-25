package com.yor42.projectazure.gameobject.entity.companion.bonus;

import com.tac.guns.client.render.pose.OneHandedPose;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.ISpellUser;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.mixin.FurnaceAccessors;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TieredItem;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static com.yor42.projectazure.libs.enums.EntityType.REUNION;

public class EntityTalulah extends AbstractEntityCompanion implements IAknOp, IMeleeAttacker, ISpellUser, IWorldSkillUseable {
    public EntityTalulah(EntityType<? extends TameableEntity> type, World worldIn) {
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
            return PlayState.STOP;
        }
        else if(this.isUsingWorldSkill()){
            event.getController().setAnimation(builder.addAnimation("worldskill", true));
            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
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

        if(this.isOrderedToSit()){
            if(this.getMainHandItem().getItem() instanceof TieredItem){
                event.getController().setAnimation(builder.addAnimation("sit_idle_arm_toolmainhand", true));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_idle_arm_emptymainhand", true));
            }
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
        else if(this.isMoving()) {
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
    protected void openGUI(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new ContainerAKNInventory.Supplier(this),buf -> buf.writeInt(this.getId()));
    }

    @Nonnull
    @Override
    public enums.CompanionRarity getRarity() {
        return enums.CompanionRarity.SPECIAL;
    }

    @Override
    public int MeleeAttackAnimationLength() {
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
    public boolean canUseWorldSkill(ServerWorld world, BlockPos pos, AbstractEntityCompanion companion) {
        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof AbstractFurnaceTileEntity){
            AbstractFurnaceTileEntity furnace = (AbstractFurnaceTileEntity)te;
            IRecipeType<? extends AbstractCookingRecipe> recipe = ((FurnaceAccessors) furnace).getRecipeType();
            Optional<? extends AbstractCookingRecipe> output = furnace.getLevel().getRecipeManager().getRecipeFor(recipe, furnace, furnace.getLevel());
            if(!output.isPresent())
                return false;
            ItemStack fuelstack = furnace.getItem(1);
            if(ForgeHooks.getBurnTime(fuelstack, recipe)>0 || (fuelstack.isEmpty() && ((FurnaceAccessors) furnace).getDataAccess().get(0)>10)){
                return false;
            }
            ItemStack existingOutput = furnace.getItem(2);
            if(existingOutput.isEmpty())
                return true;
            ItemStack outStack = output.get().getResultItem();
            if(!existingOutput.sameItem(outStack))
                return false;
            int stackSize = existingOutput.getCount()+outStack.getCount();
            return stackSize <= furnace.getMaxStackSize()&&stackSize <= outStack.getMaxStackSize();
        }
        return false;
    }

    @Override
    public boolean executeWorldSkill(ServerWorld world, BlockPos pos, AbstractEntityCompanion entity) {
        return false;
    }

    @Override
    public float getWorldSkillRange() {
        return 2.5F;
    }

    @Override
    public boolean executeWorldSkillTick(ServerWorld world, BlockPos pos, AbstractEntityCompanion entity) {
        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof AbstractFurnaceTileEntity){
            AbstractFurnaceTileEntity furnace = (AbstractFurnaceTileEntity)te;
            ((FurnaceAccessors) furnace).getDataAccess().set(0, 10);
            this.level.setBlock(pos, this.level.getBlockState(pos).setValue(AbstractFurnaceBlock.LIT, true), 3);
            furnace.setChanged();
            this.addMorale(-0.025);
        }
        return false;
    }

    @Override
    public void StartMeleeAttackingEntity() {
        this.setMeleeAttackDelay((int) (this.MeleeAttackAnimationLength() *this.getAttackSpeedModifier(this.isTalentedWeaponinMainHand())));
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
    public Hand getSpellUsingHand() {
        return Hand.OFF_HAND;
    }

    @Override
    public boolean shouldUseSpell() {
        if(this.getGunStack().getItem() instanceof GunItem) {
            boolean hasAmmo = this.getGunStack().getOrCreateTag().getInt("AmmoCount") > 0;
            boolean reloadable = this.HasRightMagazine(this.getGunStack());

            return !(hasAmmo || reloadable);
        }
        else return this.getItemInHand(getSpellUsingHand()).isEmpty() && !this.isSwimming() && !this.shouldUseNonVanillaAttack(this.getTarget());
    }

    @Override
    public void ShootProjectile(World world, @Nonnull LivingEntity target) {
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

    public static AttributeModifierMap.MutableAttribute MutableAttribute()
    {
        return MobEntity.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, PAConfig.CONFIG.TalulahMovementSpeed.get())
                .add(ForgeMod.SWIM_SPEED.get(), PAConfig.CONFIG.TalulahSwimSpeed.get())
                .add(Attributes.MAX_HEALTH, PAConfig.CONFIG.TalulahHealth.get())
                .add(Attributes.ATTACK_DAMAGE, PAConfig.CONFIG.TalulahAttackDamage.get())
                ;
    }
}
