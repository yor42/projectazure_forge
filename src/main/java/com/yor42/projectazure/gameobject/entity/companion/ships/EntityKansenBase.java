package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.enums.AmmoCategory;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

import static com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase.CONTROLLER_NAME;
import static com.yor42.projectazure.libs.enums.SLOTTYPE.*;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static com.yor42.projectazure.libs.utils.MathUtil.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

public abstract class EntityKansenBase extends AbstractEntityCompanion {
    private static final UUID SAILING_SPEED_MODIFIER = UUID.randomUUID();
    private static final AttributeModifier SAILING_SPEED_BOOST = new AttributeModifier(SAILING_SPEED_MODIFIER, "Rigging Swim speed boost",1.5F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private static final EntityDataAccessor<Integer> SHIP_FIRE_TICK = SynchedEntityData.defineId(EntityKansenBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> ITEM_RIGGING = SynchedEntityData.defineId(EntityKansenBase.class, EntityDataSerializers.ITEM_STACK);

    public ItemStackHandler RiggingSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
                EntityKansenBase.this.entityData.set(ITEM_RIGGING, this.getStackInSlot(0));
        }

        @Override
        protected void onLoad() {
            EntityKansenBase.this.entityData.set(ITEM_RIGGING, this.getStackInSlot(0));
        }
    };

    @Nullable
    @Override
    protected Item getLimitBreakItem() {
        if(this.getLimitBreakLv() == 0){
            return RegisterItems.COGNITIVE_CHIP.get();
        }
        return RegisterItems.COGNITIVE_ARRAY.get();
    }

    @Override
    public enums.EntityType getEntityType() {
        return enums.EntityType.KANSEN;
    }


    protected enums.shipClass shipclass;

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected EntityKansenBase(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.AmmoStorage.setSize(8);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHIP_FIRE_TICK, 0);
        this.entityData.define(ITEM_RIGGING,ItemStack.EMPTY);
    }

    public boolean isShipOnFire(){
        return this.getShipFireTicks()>0;
    }

    public int getShipFireTicks(){
        return this.entityData.get(SHIP_FIRE_TICK);
    }

    public void ForceShipFireTicks(int tick){
        this.entityData.set(SHIP_FIRE_TICK, tick);
    }

    public void setShipFire(int seconds) {
        int i = seconds * 20;
        i = ProtectionEnchantment.getFireAfterDampener(this, i);

        if (this.getShipFireTicks() < i) {
            this.ForceShipFireTicks(i);
        }
    }

    public void ExtinguishShipfire(){
        this.ForceShipFireTicks(0);
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("shipfire", this.getShipFireTicks());
        compound.put("rigging",this.RiggingSlot.serializeNBT());
    }

    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.ForceShipFireTicks(compound.getInt("shipfire"));
        if(compound.contains("rigging"))
            this.RiggingSlot.deserializeNBT(compound.getCompound("rigging"));
    }

    public void setShipClass(enums.shipClass setclass){
        this.shipclass = setclass;
    }

    public enums.shipClass getShipClass(){
        return this.shipclass;
    }

    public boolean canUseRigging(){
        ItemStack rigging = this.getRigging();
        Item RiggingItem = rigging.getItem();
        if(!(RiggingItem instanceof ItemRiggingBase)){
            return false;
        }

        if(!((ItemRiggingBase) RiggingItem).isShipRigging()){
            return false;
        }

        return rigging.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map((fluidtank)-> {
            FluidStack stack = fluidtank.drain(1, IFluidHandler.FluidAction.SIMULATE);
            return !stack.isEmpty();}).orElse(false);
    }

    public boolean isSailing(){
        return this.isInWater() && this.canUseRigging();
    }

    public ItemStack getRigging(){
        if(this.getCommandSenderWorld().isClientSide()) {
            return this.entityData.get(ITEM_RIGGING);
        }
        else{
            return this.getShipRiggingStorage().getStackInSlot(0);
        }
    }

    @Override
    public int getMaxLevel(){
        return 70+(this.getLimitBreakLv()*10+(this.getAwakeningLv()*10));
    }


    public boolean hasRigging(){

        if(!(this.getRigging().getItem() instanceof ItemRiggingBase)){
            return false;
        }
        else return ((ItemRiggingBase) this.getRigging().getItem()).getValidclass().test(this);
    }

    @Nullable
    public IItemHandler getHanger() {
        if (this.hasRigging()) {
            IItemHandler hangar = MultiInvUtil.getCap(this.getRigging()).getInventory(enums.SLOTTYPE.PLANE.ordinal());
            if (hangar.getSlots() > 0) {
                return hangar;
            }
        }
        return null;
    }

    public int getPlanetoLaunch(){
        return 1;
    }

    public void attackEntityFromCannon(DamageSource source, AmmoProperties property, double distanceMultiplier) {
        if(rollBooleanRNG((float) (property.getRawhitChance()*distanceMultiplier))) {
            if (this.hasRigging()) {
                DamageItem(property.getDamage(enums.DamageType.RIGGING), this.getRigging());
                DamageComponent(this, property.getDamage(enums.DamageType.COMPONENT), this.getRigging(), property.isExplosive());
            }
            super.hurt(source, property.getDamage(enums.DamageType.ENTITY));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player && this.isOwnedBy((LivingEntity) source.getEntity())) {
            if (this.toldtomovecount >= 3) {
                this.toldtomovecount = 0;
                Vec3 loc = this.WanderRNG();
                if (loc != null) {
                    this.getNavigation().moveTo(loc.x, loc.y, loc.z, 1);
                }
            } else {
                this.toldtomovecount += 1;
            }
            return false;
        }
        float DamageMultiplier = 1.0F;
        float ignoreArmorChance = RangedFloatRandom(0.05F, 0.1F);
        if (this.hasRigging() && (!(source == DamageSource.FALL) || !(source == DamageSource.GENERIC && rollBooleanRNG(ignoreArmorChance)))) {
            DamageMultiplier = getRiggingedDamageModifier();
            DamageItem(amount, this.getRigging());
            this.playSound(SoundEvents.IRON_GOLEM_HURT, 1.0F, 1.0f);
        }
        return super.hurt(source, amount * DamageMultiplier);
    }



    @Override
    public void aiStep() {
        super.aiStep();

        AttributeInstance modifiableattributeinstance = this.getAttribute(ForgeMod.SWIM_SPEED.get());
        if (this.isSailing()) {
            this.kansenFloat();
            if (modifiableattributeinstance != null && modifiableattributeinstance.getModifier(SAILING_SPEED_MODIFIER) == null) {
                modifiableattributeinstance.addTransientModifier(SAILING_SPEED_BOOST);
            }
            Vec3 vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            if(!(vector3d.x == 0 || vector3d.z == 0)) {
                this.level.addParticle(ParticleTypes.CLOUD, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            }
            if(!PAConfig.CONFIG.RiggingInfiniteFuel.get() && this.tickCount%2 == 0) {
                this.getRigging().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent((fluidtank) -> fluidtank.drain(1, IFluidHandler.FluidAction.EXECUTE));
            }
        }
        else if(modifiableattributeinstance != null){
            modifiableattributeinstance.removeModifier(SAILING_SPEED_BOOST);
        }

        if(this.getRigging().getItem() instanceof ItemRiggingBase){
            ((ItemRiggingBase) this.getRigging().getItem()).onUpdate(this.getRigging());
        }

        if(this.isShipOnFire()){
            if (this.getShipFireTicks() % 20 == 0) {
                this.hurt(DamageSource.ON_FIRE, 1.0F);
            }

            this.ForceShipFireTicks(this.getShipFireTicks() - 1);
        }
    }

    @Override
    public boolean displayFireAnimation() {
        return super.displayFireAnimation() || this.isShipOnFire();
    }

    @Override
    public boolean canStandOnFluid(Fluid p_230285_1_) {
        return this.canUseRigging() && p_230285_1_.is(FluidTags.WATER);
    }

    public ItemStack findAmmo(enums.AmmoCategory category){
        for (int i=0; i<this.AmmoStorage.getSlots();i++){
            Item AmmoItem = this.AmmoStorage.getStackInSlot(i).getItem();
            if(AmmoItem instanceof ItemCannonshell){
                if(category == ((ItemCannonshell) AmmoItem).getAmmoProperty().getCategory()){
                    return this.AmmoStorage.getStackInSlot(i);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUseCannonOrTorpedo() {
        return this.canUseRigging() && (canUseCannon(this, this.getRigging()) || canUseTorpedo(this, this.getRigging()));
    }

    public boolean canUseShell(enums.AmmoCategory types){
        return findAmmo(types) != ItemStack.EMPTY;
    }

    public ItemStackHandler getShipRiggingStorage() {
        return this.RiggingSlot;
    }

    public AmmoCategory getActiveShellCategory(){
        return AmmoCategory.GENERIC;
    }

    public void AttackUsingCannon(LivingEntity target){
        boolean shouldFire = this.canUseShell(getActiveShellCategory()) && this.canUseRigging() && canUseCannon(this, this.getRigging());

        if(shouldFire) {
            ItemStack rigging = this.getRigging();
            Item RiggingItem = rigging.getItem();
            ItemStack Ammostack = this.findAmmo(this.getActiveShellCategory());
            if(!(Ammostack.getItem() instanceof ItemCannonshell)){
                return;
            }

            getPreparedWeapon(this, rigging, MAIN_GUN, SUB_GUN).ifPresent(
                    (pair)->{
                        enums.SLOTTYPE cannonType = pair.getFirst();
                        int slotindex = pair.getSecond();
                        ItemStack FiringCannon = MultiInvUtil.getCap(rigging).getInventory(cannonType.ordinal()).getStackInSlot(slotindex);

                        if(!(FiringCannon.getItem() instanceof ItemEquipmentBase)){
                            return;
                        }
                        Vec3 vector3d = this.getViewVector(1.0F);
                        double x = target.getX() - (this.getX());
                        double y = target.getY(0.5) - (this.getY(0.5));
                        double z = target.getZ() - (this.getZ());
                        AmmoProperties properties = ((ItemCannonshell) Ammostack.getItem()).getAmmoProperty();
                        EntityCannonPelllet shell = new EntityCannonPelllet(this.level, this, 0, 0, 0, properties);
                        Vec3 offset = new Vec3(-0.5F,0,0).yRot(this.yBodyRot);
                        shell.setPos(this.getX()+offset.x, this.getY(0.5)+offset.y, this.getZ()+offset.z);
                        shell.shoot(x,y,z, 1F, 0.05F);
                        this.playSound(registerSounds.CANON_FIRE_MEDIUM, 1.0F, (MathUtil.getRand().nextFloat() - MathUtil.getRand().nextFloat()) * 0.2F + 1.0F);
                        this.level.addFreshEntity(shell);
                        Ammostack.shrink(1);
                        Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(()->this), new spawnParticlePacket(this, spawnParticlePacket.Particles.CANNON_SMOKE, vector3d.x, vector3d.y, vector3d.z));

                        if(RiggingItem instanceof ItemRiggingBase && !this.getCommandSenderWorld().isClientSide()) {
                            ItemRiggingBase riggingBase = (ItemRiggingBase) RiggingItem;
                            final int id = GeckoLibUtil.guaranteeIDForStack(rigging, (ServerLevel) this.getCommandSenderWorld());
                            final AnimationController controller = GeckoLibUtil.getControllerForID(riggingBase.factory, id, CONTROLLER_NAME);
                            Pair<String, Integer> animationpair = riggingBase.getFireAnimationname(cannonType, slotindex);
                            if (animationpair != null) {
                                String animationname = animationpair.getFirst();
                                int animationid = animationpair.getSecond();
                                final PacketDistributor.PacketTarget packettarget = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this);
                                GeckoLibNetwork.syncAnimation(packettarget, riggingBase, id, animationid);
                                controller.markNeedsReload();
                                controller.setAnimation(new AnimationBuilder().addAnimation(animationname, PLAY_ONCE));
                            }
                        }

                        this.addExp(0.5F);
                        setEquipmentDelay(FiringCannon);
                        Vec3 recoil = new Vec3(0.3F, 0, 0).yRot(this.yBodyRot);
                        Vec3 vec = this.getDeltaMovement().add(recoil);
                        this.setDeltaMovement(vec);
                        this.addMorale(-0.2);
                        this.startPlayingShipAttackAnim();
                    }
            );

        }
    }

    public void AttackUsingTorpedo(LivingEntity target){
        if(!this.isSailing()){
            return;
        }
        ItemStack rigging = this.getRigging();
        Item RiggingItem = rigging.getItem();
        getPreparedWeapon(this, rigging, TORPEDO).ifPresent((pair)->{
            enums.SLOTTYPE slottype = pair.getFirst();
            int index = pair.getSecond();

            ItemStack torpedostack = MultiInvUtil.getCap(rigging).getInventory(slottype.ordinal()).getStackInSlot(index);

            Vec3 vector3d = this.getViewVector(1.0F);
            double d2 = target.getX() - (this.getX() + vector3d.x * 4.0D);
            double d3 = target.getY(0.5D) - this.getY() - 0.5D;
            double d4 = target.getZ() - (this.getZ() + vector3d.z * 4.0D);

            EntityProjectileTorpedo torpedo = new EntityProjectileTorpedo(this, d2, d3, d4, this.level);
            torpedo.setPos(this.getX() + vector3d.x, this.getY() - 0.5D, torpedo.getZ() + vector3d.z);
            this.level.addFreshEntity(torpedo);
            this.addExp(1.0F);
            ItemStackUtils.useAmmo(torpedostack);
            setEquipmentDelay(torpedostack);

            if(RiggingItem instanceof ItemRiggingBase && !this.getCommandSenderWorld().isClientSide()) {
                ItemRiggingBase riggingBase = (ItemRiggingBase) RiggingItem;
                final int id = GeckoLibUtil.guaranteeIDForStack(rigging, (ServerLevel) this.getCommandSenderWorld());
                final AnimationController controller = GeckoLibUtil.getControllerForID(riggingBase.factory, id, CONTROLLER_NAME);
                Pair<String, Integer> animationpair = riggingBase.getFireAnimationname(slottype, index);
                if (animationpair != null) {
                    String animationname = animationpair.getFirst();
                    int animationid = animationpair.getSecond();
                    final PacketDistributor.PacketTarget packettarget = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this);
                    GeckoLibNetwork.syncAnimation(packettarget, riggingBase, id, animationid);
                    controller.markNeedsReload();
                    controller.setAnimation(new AnimationBuilder().addAnimation(animationname, PLAY_ONCE));
                }
            }
            this.addMorale(-0.15);

        });
    }

    private void kansenFloat() {

        if (this.isInWater()) {
            CollisionContext iselectioncontext = CollisionContext.of(this);
            if (iselectioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level.getFluidState(this.blockPosition().above()).is(FluidTags.WATER)) {
                this.onGround = true;
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
            }
        }
    }

    @Override
    public boolean canUseSkill(LivingEntity target) {
        return super.canUseSkill(target) && this.canUseCannonOrTorpedo() && this.isSailing();
    }
}
