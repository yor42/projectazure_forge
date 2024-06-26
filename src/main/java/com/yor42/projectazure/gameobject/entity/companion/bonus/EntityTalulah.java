package com.yor42.projectazure.gameobject.entity.companion.bonus;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.interfaces.ISpellUser;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.mixin.FurnaceAccessors;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static com.yor42.projectazure.libs.enums.EntityType.REUNION;

public class EntityTalulah extends AbstractEntityCompanion implements IAknOp, IMeleeAttacker, ISpellUser, IWorldSkillUseable {
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

        if(this.isOnPlayersBack()){
            event.getController().setAnimation(builder.addAnimation("carry_arm"));
            return PlayState.CONTINUE;
        }

        if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_arm", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("faint_arm_idle", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.swinging){
            return PlayState.STOP;
        }
        else if(this.isUsingWorldSkill()){
            event.getController().setAnimation(builder.addAnimation("worldskill", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.entityData.get(ECCI_ANIMATION_TIME)>0 && !this.isAngry()){
            event.getController().setAnimation(builder.addAnimation("lewd", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isUsingSpell()){
            event.getController().setAnimation(builder.addAnimation("rangedattack_arm", ILoopType.EDefaultLoopTypes.LOOP));
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
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        
        else if(this.isBlocking()){
            event.getController().setAnimation(builder.addAnimation("shield_block", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isGettingHealed()){
            event.getController().setAnimation(builder.addAnimation("heal_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_arm", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isOrderedToSit() || this.getVehicle() != null){
            if(this.getMainHandItem().getItem() instanceof TieredItem){
                event.getController().setAnimation(builder.addAnimation("sit_idle_arm_toolmainhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(builder.addAnimation("sit_idle_arm_emptymainhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        else if(this.getMainHandItem().getItem() instanceof GunItem){
            return PlayState.STOP;
        }
        else if(this.isMoving()) {
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
        }
        else if(this.isDeadOrDying()){
            event.getController().setAnimation(builder.addAnimation("faint_leg", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("faint_leg_idle", ILoopType.EDefaultLoopTypes.LOOP));
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
                    event.getController().setAnimation(builder.addAnimation("sit_leg").addAnimation("sit_idle_leg", ILoopType.EDefaultLoopTypes.LOOP));
                }
            return PlayState.CONTINUE;
        }else if(this.isSwimming()) {
            event.getController().setAnimation(builder.addAnimation("swim_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if(this.isNonVanillaMeleeAttacking()){
            event.getController().setAnimation(builder.addAnimation("meleeattack_leg", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        else if (isMoving()) {
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
    protected <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> event) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }

        AnimationBuilder builder = new AnimationBuilder();

        if(this.isOrderedToSit() && this.getVehicle() == null){
            event.getController().setAnimation(builder.addAnimation("sit_tail").addAnimation("sit_tail_idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else if(this.isSleeping()){
            event.getController().setAnimation(builder.addAnimation("sleep_tail", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else if(this.isBeingPatted()){
            event.getController().setAnimation(builder.addAnimation("pat_tail", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else{
            event.getController().setAnimation(builder.addAnimation("idle_tail", ILoopType.EDefaultLoopTypes.LOOP));
        }

        return PlayState.CONTINUE;
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
        return new ArrayList<>(Collections.singletonList(9));
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
    public boolean shouldUseNonVanillaAttack(LivingEntity target) {
        return this.hasMeleeItem() && this.distanceTo(target) <=this.getAttackRange(this.isTalentedWeaponinMainHand());
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
    public boolean canUseWorldSkill(ServerLevel world, BlockPos pos, AbstractEntityCompanion companion) {
        BlockEntity te = world.getBlockEntity(pos);
        if(te instanceof AbstractFurnaceBlockEntity furnace){
            RecipeType<? extends AbstractCookingRecipe> recipe = ((FurnaceAccessors) furnace).getRecipeType();
            Optional<Recipe<AbstractFurnaceBlockEntity>> output = this.getLevel().getRecipeManager().getRecipeFor((RecipeType)recipe, furnace, this.getLevel());
            if(output.isEmpty())
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
    public boolean executeWorldSkill(ServerLevel world, BlockPos pos, AbstractEntityCompanion entity) {
        return false;
    }

    @Override
    public double getWorldSkillRange() {
        return 2.5D;
    }

    @Override
    public boolean executeWorldSkillTick(ServerLevel world, BlockPos pos, AbstractEntityCompanion entity) {
        BlockEntity te = world.getBlockEntity(pos);
        if(te instanceof AbstractFurnaceBlockEntity){
            AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity)te;
            ((FurnaceAccessors) furnace).getDataAccess().set(0, 10);
            this.level.setBlock(pos, this.level.getBlockState(pos).setValue(AbstractFurnaceBlock.LIT, true), 3);
            furnace.setChanged();
            this.addMorale(-0.025);
        }
        return false;
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
    public boolean shouldUseSpell(LivingEntity Target) {
        if(this.getGunStack().getItem() instanceof GunItem) {
            boolean hasAmmo = this.getGunStack().getOrCreateTag().getInt("AmmoCount") > 0;
            boolean reloadable = this.HasRightMagazine(this.getGunStack());

            return !(hasAmmo || reloadable);
        }
        else return this.getItemInHand(getSpellUsingHand()).isEmpty() && !this.isSwimming() && this.RangedAttackCoolDown<=0;
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
    public int SpellCooldown() {
        return 100;
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
