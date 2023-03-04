package com.yor42.projectazure.gameobject.entity.companion.bonus;

import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.interfaces.ISpellUser;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.mixin.FurnaceAccessors;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.client.Minecraft;
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
import software.bernie.geckolib3.core.builder.ILoopType;
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
            if(this.getUsedItemHand() == Hand.MAIN_HAND){
                event.getController().setAnimation(builder.addAnimation("eat_mainhand", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else if(this.getUsedItemHand() == Hand.OFF_HAND){
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

    @Override
    public void openGUI(ServerPlayerEntity player) {
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
    public boolean canUseWorldSkill(ServerWorld world, BlockPos pos, AbstractEntityCompanion companion) {
        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof AbstractFurnaceTileEntity){
            AbstractFurnaceTileEntity furnace = (AbstractFurnaceTileEntity)te;
            IRecipeType<? extends AbstractCookingRecipe> recipe = ((FurnaceAccessors) furnace).getRecipeType();
            Optional<? extends AbstractCookingRecipe> output = this.getCommandSenderWorld().getRecipeManager().getRecipeFor(recipe, furnace, this.getCommandSenderWorld());
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
    public boolean shouldUseSpell(LivingEntity Target) {
        if(this.getGunStack().getItem() instanceof GunItem) {
            boolean hasAmmo = this.getGunStack().getOrCreateTag().getInt("AmmoCount") > 0;
            boolean reloadable = this.HasRightMagazine(this.getGunStack());

            return !(hasAmmo || reloadable);
        }
        else return this.getItemInHand(getSpellUsingHand()).isEmpty() && !this.isSwimming() && this.RangedAttackCoolDown<=0;
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
