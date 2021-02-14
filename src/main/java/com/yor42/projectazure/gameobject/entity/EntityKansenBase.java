package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.DamageSources;
import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.ai.*;
import com.yor42.projectazure.gameobject.items.ItemAmmo;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.DamageComponent;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.DamageRiggingorEquipment;

public abstract class EntityKansenBase extends TameableEntity implements IAnimatable {

    Random rand = new Random();

    private static final DataParameter<CompoundNBT> STORAGE = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OPENINGDOOR = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ITEM_RIGGING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<Integer> MAXPATEFFECTCOUNT = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PATEFFECTCOUNT = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PATCOOLDOWN = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> OATHED = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);


    public ItemStackHandler ShipStorage = new ItemStackHandler(13) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot == 0) {
                EntityKansenBase.this.dataManager.set(ITEM_RIGGING, this.getStackInSlot(slot));
            }
        }
    };

    public ItemStackHandler AmmoStorage = new ItemStackHandler(8);

    public final IItemHandlerModifiable EQUIPMENT = new IItemHandlerModifiable() {

        private final EquipmentSlotType[] EQUIPMENTSLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};


        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            EquipmentSlotType slottype;

            switch (slot) {
                case 1:
                    slottype = EquipmentSlotType.OFFHAND;
                    break;
                case 2:
                    slottype = EquipmentSlotType.HEAD;
                    break;
                case 3:

                    slottype = EquipmentSlotType.CHEST;
                    break;
                case 4:
                    slottype = EquipmentSlotType.LEGS;
                    break;
                case 5:
                    slottype = EquipmentSlotType.FEET;
                    break;
                default:
                    slottype = EquipmentSlotType.MAINHAND;
            }
            EntityKansenBase.this.setItemStackToSlot(slottype, stack);
        }

        @Override
        public int getSlots() {
            return 6;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            EquipmentSlotType slottype;

            switch (slot) {
                case 1:
                    slottype = EquipmentSlotType.OFFHAND;
                    break;
                case 2:
                    slottype = EquipmentSlotType.HEAD;
                    break;
                case 3:
                    slottype = EquipmentSlotType.CHEST;
                    break;
                case 4:
                    slottype = EquipmentSlotType.LEGS;
                    break;
                case 5:
                    slottype = EquipmentSlotType.FEET;
                    break;
                default:
                    slottype = EquipmentSlotType.MAINHAND;
            }
            return EntityKansenBase.this.getItemStackFromSlot(slottype);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

            if(!isItemValid(slot, stack)){
                return stack;
            }
            ItemStack existingstack = getStackInSlot(slot);
            int limit = this.getSlotLimit(slot);

            if(!existingstack.isEmpty()){
                if (!ItemHandlerHelper.canItemStacksStack(stack, existingstack)) {
                    return stack;
                }
                limit -= existingstack.getCount();
            }

            if (limit <= 0)
                return stack;

            boolean reachedLimit = stack.getCount() > limit;

            if (!simulate)
            {
                if (existingstack.isEmpty()) {
                    setStackInSlot(slot, stack);
                }
                else {
                        existingstack.grow(reachedLimit ? limit : stack.getCount());
                    setStackInSlot(slot, existingstack);
                    }
            }
            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0)
                return ItemStack.EMPTY;

            ItemStack existing = getStackInSlot(slot);

            if (existing.isEmpty())
                return ItemStack.EMPTY;

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract)
            {
                if (!simulate)
                {
                    setStackInSlot(slot, ItemStack.EMPTY);
                    return existing;
                }
                else
                {
                    return existing.copy();
                }
            }
            else
            {
                if (!simulate)
                {
                    setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                }

                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            switch (slot){
                case 0:
                case 1:
                    return 64;
                default:
                    return 1;
            }
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch(slot){
                case 0:
                case 1:
                    return true;
                default:
                    if(stack.getItem() instanceof ArmorItem) {
                        return stack.canEquip(EQUIPMENTSLOTS[slot-2], EntityKansenBase.this);
                    }
                    else return false;
            }
        }
    };

    protected int level,  patAnimationTime, LimitBreakLv, patTimer;
    protected double affection, exp;
    protected boolean isMeleeing, isOpeningDoor;
    protected int MaxAmmoCount, awakeningLevel;
    protected enums.shipClass shipclass;

    protected EntityKansenBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.setLevel(0);
        this.setExp(0);
        this.setMaxAmmoCount(350);
    }

    public int getMaxAmmoCount() {
        return this.MaxAmmoCount;
    }

    public void setMaxAmmoCount(int maxAmmoCount) {
        this.MaxAmmoCount = maxAmmoCount;
    }

    public void setOathed(boolean bool){
        this.dataManager.set(OATHED, bool);
    }

    public boolean isOathed(){
        return this.dataManager.get(OATHED);
    }

    public abstract int getRiggingOffset();

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean addLevel(int deltaLevel){
        if(this.getLevel()+deltaLevel > this.getMaxLevel()){
            return false;
        }
        this.setLevel(this.level += deltaLevel);
        return true;
    }

    public int getMaxLevel(){
            switch (this.LimitBreakLv) {
                default:
                    return 70;
                case 1:
                    return 80;
                case 2:
                    return 90;
                case 3:
                    switch (this.getAwakeningLevel()){
                        default:
                            return 100;
                        case 1:
                            return 110;
                        case 2:
                            return 120;
                    }
            }
    }

    protected int getAwakeningLevel(){
        return this.awakeningLevel;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getExp() {
        return this.exp;
    }

    public double addExp(double deltaExp){
        if(this.getExp()+deltaExp >= this.getMaxExp()) {
            this.exp = this.getExp()+deltaExp;
            this.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            while(this.exp>this.getMaxExp() && this.getLevel() <= this.getMaxLevel()){
                this.addLevel(1);
                this.exp-=this.getMaxExp();
            }
        }
        this.exp+=deltaExp;
        return this.exp;
    }

    public double getMaxExp(){
        return 10+(5*this.getLevel());
    }

    public int getLimitBreakLv() {
        return this.LimitBreakLv;
    }

    public void setLimitBreakLv(int value) {
        this.LimitBreakLv = value;
    }

    public void addLimitBreak(int delta){
        this.LimitBreakLv += delta;
    }

    public void doLimitBreak(){
        this.addLimitBreak(1);
    }

    public double getAffection() {
        return this.affection;
    }

    public void setAffection(double affection){
        this.affection = affection;
    }

    public double getmaxAffextion(){
        return this.isOathed()? 200:100;
    };

    public void addAffection(double Delta){
        this.setAffection(Math.min(this.getAffection() + Delta, this.getmaxAffextion()));
    }

    public abstract enums.ShipRarity getRarity();

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STORAGE, new CompoundNBT());
        this.dataManager.register(SITTING, this.isSitting());
        this.dataManager.register(ITEM_RIGGING,ItemStack.EMPTY);
        this.dataManager.register(OPENINGDOOR, false);
        this.dataManager.register(MELEEATTACKING, false);
        this.dataManager.register(MAXPATEFFECTCOUNT, 0);
        this.dataManager.register(PATEFFECTCOUNT, 0);
        this.dataManager.register(PATCOOLDOWN, 0);
        this.dataManager.register(OATHED, false);
    }

    public void setOpeningdoor(boolean openingdoor){
        this.isOpeningDoor = openingdoor;
        this.dataManager.set(OPENINGDOOR, this.isOpeningDoor);
    }

    public boolean isOpeningDoor() {
        return this.dataManager.get(OPENINGDOOR);
    }

    public void setMeleeing(boolean attacking){
        this.isMeleeing = attacking;
        this.dataManager.set(MELEEATTACKING, this.isMeleeing);

    }

    public boolean isMeleeing(){
        return this.dataManager.get(MELEEATTACKING);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putDouble("affection", this.affection);
        compound.putInt("patcooldown", this.dataManager.get(PATCOOLDOWN));
        compound.put("inventory",this.ShipStorage.serializeNBT());
        compound.put("ammoStorage", this.AmmoStorage.serializeNBT());
        compound.putBoolean("oathed", this.dataManager.get(OATHED));
        compound.putDouble("exp", this.exp);
        compound.putInt("level", this.level);
        compound.putInt("limitbreaklv", this.LimitBreakLv);
        compound.putInt("awaken", this.awakeningLevel);
        compound.putInt("maxammocount", this.getMaxAmmoCount());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.affection = compound.getFloat("affection");
        this.dataManager.set(PATCOOLDOWN, compound.getInt("patcooldown"));
        this.dataManager.set(OATHED, compound.getBoolean("oathed"));
        if(compound.contains("inventory"))
            this.ShipStorage.deserializeNBT((CompoundNBT) compound.get("inventory"));
        if(compound.contains("ammoStorage"))
            this.AmmoStorage.deserializeNBT((CompoundNBT) compound.get("ammoStorage"));
        this.level = compound.getInt("level");
        this.exp = compound.getFloat("exp");
        this.LimitBreakLv = compound.getInt("limitbreaklv");
        this.awakeningLevel = compound.getInt("awaken");
        this.setMaxAmmoCount(compound.getInt("maxammocount"));

    }

    public void setShipClass(enums.shipClass setclass){
        this.shipclass = setclass;
    }

    public enums.shipClass getShipClass(){
        return this.shipclass;
    }

    public boolean canUseRigging(){
        return this.getRigging().getItem() instanceof ItemRiggingBase; //&& !this.getRigging.isDestroyed
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    public boolean isChild() {
        return false;
    }

    public boolean Hasrigging(){
        return this.getRigging().getItem() instanceof ItemRiggingBase;
    }

    public boolean Sailing(){
        float f = this.getEyeHeight() - 1F;
        return this.isInWater() && this.func_233571_b_(FluidTags.WATER) > (double)f && this.Hasrigging();
        //return this.isInWater() && this.func_233571_b_(FluidTags.WATER) > (double)f && this.Hasrigging();
    }

    @Override
    public void setTamedBy(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerId(player.getUniqueID());
    }

    public boolean canUseCanon() {

        if(this.getRigging().getItem() instanceof ItemRiggingBase){
            ItemRiggingBase RiggingItem = (ItemRiggingBase) this.getRigging().getItem();
            return RiggingItem.canUseCanon(this.getRigging());
        }
        else return false;
    }

    public boolean canUseTorpedo() {
        if(this.getRigging().getItem() instanceof ItemRiggingBase){
            ItemRiggingBase RiggingItem = (ItemRiggingBase) this.getRigging().getItem();
            return RiggingItem.canUseTorpedo(this.getRigging());
        }
        else return false;
    }

    public ItemStack getRigging(){
        return this.dataManager.get(ITEM_RIGGING);
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if(this.isOwner(player)){
            if(player.isSneaking()){
                if(!world.isRemote) {
                    openGUI(player);
                    return ActionResultType.SUCCESS;
                }
            }
            else{
                if(player.getHeldItemMainhand() == ItemStack.EMPTY) {
                    this.beingpatted();
                }
                else if(player.getHeldItemMainhand() != ItemStack.EMPTY) {

                    if (player.getHeldItem(hand).getItem() == registerItems.OATHRING.get()) {
                        if (this.getAffection() < 100 && !player.isCreative()) {
                            player.sendMessage(new TranslationTextComponent("entity.not_enough_affection"), this.getUniqueID());
                            return ActionResultType.FAIL;
                        } else {
                            if (!this.isOathed()) {
                                this.setOathed(true);
                                if (player.isCreative()) {
                                    this.setAffection(100);
                                } else {
                                    player.getHeldItem(hand).shrink(1);
                                }
                                return ActionResultType.CONSUME;
                            }
                        }
                    }
                    else {
                        this.func_233687_w_(!this.isSitting());
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        else{
            if(player.getHeldItemMainhand().getItem() == registerItems.Rainbow_Wisdom_Cube.get()&&!this.isTamed()){
                this.setTamedBy(player);
                player.getHeldItem(hand).shrink(1);
                return ActionResultType.CONSUME;
            }
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    private void openGUI(PlayerEntity player){
        NetworkHooks.openGui((ServerPlayerEntity) player, new ContainerKansenInventory.Supplier(this));
        Main.PROXY.setSharedMob(this);
    }

    protected void beingpatted(){
        if(this.patAnimationTime == 0 || this.dataManager.get(MAXPATEFFECTCOUNT) == 0){
            this.dataManager.set(MAXPATEFFECTCOUNT, 5+this.rand.nextInt(3));
        }
        this.patAnimationTime = 20;
    };

    public boolean isBeingPatted() {
        return this.patAnimationTime !=0;
    }

    /*
    public boolean attackEntityCannon(DamageSources sources, float amount, enums.AmmoTypes type){
        if(this.hasRigging()){
            DamageRiggingorEquipment(amount, this.getRigging());

            DamageComponent(amount, this.getRigging(), type == enums.AmmoTypes.HE);
        }
    }

     */

    public boolean hasRigging(){
        return this.getRigging().getItem() instanceof ItemRiggingBase;
    }


    public boolean attackEntityFrom(DamageSource source, float amount) {

        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new KansenSwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new KansenRideBoatAlongPlayerGoal(this, 1.0));
        this.goalSelector.addGoal(5, new KansenMeleeGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new KansenFollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new KansenWorkGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new KansenOpenDoorGoal(this, true));
        //this.goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(11, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(12, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(13, new OwnerHurtTargetGoal(this));
    }

    @Override
    public void livingTick() {

        if(this.patAnimationTime >0){

            this.navigator.clearPath();

            this.patAnimationTime--;
            this.patTimer++;

            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            if (this.patTimer%20 == 0) {
                if (this.dataManager.get(PATEFFECTCOUNT) < this.dataManager.get(MAXPATEFFECTCOUNT)) {
                    this.dataManager.set(PATEFFECTCOUNT, this.dataManager.get(PATEFFECTCOUNT)+1);
                    this.addAffection(0.1);
                    this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);

                } else {
                    this.dataManager.set(PATEFFECTCOUNT, this.dataManager.get(MAXPATEFFECTCOUNT));
                    this.addAffection(-0.15);
                    if(this.dataManager.get(PATCOOLDOWN) == 0){
                        this.dataManager.set(PATCOOLDOWN, (7+this.rand.nextInt(5))*1200);
                    }
                    this.world.addParticle(ParticleTypes.SMOKE, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                }
            }
        }
        else{

            this.patTimer = 0;

            if(this.dataManager.get(PATCOOLDOWN) > 0){
                this.dataManager.set(PATCOOLDOWN, this.dataManager.get(PATCOOLDOWN) -1);
            }
            else{
                this.dataManager.set(MAXPATEFFECTCOUNT,0);
                this.dataManager.set(PATEFFECTCOUNT,0);
            }
        }

        if(this.affection <0)
            this.affection = 0;
        else if(this.affection>100){
            if(!this.isOathed())
                this.affection=100;
            else if(this.affection > 200){
                this.affection = 200;
            }
        }

        if(!this.isInWater() && !this.isSitting() && !this.isSleeping()) {
            this.navigator.getNodeProcessor().setCanEnterDoors(true);
            this.navigator.getNodeProcessor().setCanOpenDoors(true);
        }
        if (this.Sailing()) {
            this.kansenFloat();
        }

        if(this.getRigging().getItem() instanceof ItemRiggingBase){
            ((ItemRiggingBase) this.getRigging().getItem()).onUpdate(this.getRigging());
        }

        super.livingTick();
    }

    public ItemStack findAmmo(enums.AmmoTypes types){
        for (int i=0; i<this.AmmoStorage.getSlots();i++){
            Item AmmoItem = this.AmmoStorage.getStackInSlot(i).getItem();
            if(AmmoItem instanceof ItemAmmo){
                if(types == ((ItemAmmo) AmmoItem).getAmmoType()){
                    return this.AmmoStorage.getStackInSlot(i);
                };
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStackHandler getAmmoStorage() {
        return this.AmmoStorage;
    }

    public boolean canUseAmmo(enums.AmmoTypes types){
        return findAmmo(types) != ItemStack.EMPTY;
    }

    public void useAmmo(enums.AmmoTypes type){
        this.findAmmo(type).shrink(1);
    }

    public ItemStackHandler getShipStorage() {
        return ShipStorage;
    }

    public IItemHandlerModifiable getEquipment(){
        return this.EQUIPMENT;
    }

    private void kansenFloat() {
        Vector3d vec3d = this.getMotion();
        this.setVelocity(vec3d.x * 0.9900000095367432D, vec3d.y + (double)(vec3d.y < 0.05999999865889549D ? 5.0E-4F : 0.0F), vec3d.z * 0.9900000095367432D);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        EntityKansenBase.this.dataManager.set(ITEM_RIGGING, this.getShipStorage().getStackInSlot(0));
        return super.createSpawnPacket();
    }

}
