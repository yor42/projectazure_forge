package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.ai.KansenSwimGoal;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
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
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public abstract class EntityKansenBase extends TameableEntity implements IAnimatable {

    Random rand = new Random();

    private final AnimationFactory factory = new AnimationFactory(this);

    private static final DataParameter<Boolean> DATA_ID_RIGGING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);
    public static final DataParameter<CompoundNBT> STORAGE = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);

    public static final DataParameter<ItemStack> ITEM_RIGGING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.ITEMSTACK);


    public ItemStackHandler ShipStorage = new ItemStackHandler(13) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot == 0) {
                EntityKansenBase.this.dataManager.set(ITEM_RIGGING, this.getStackInSlot(slot));
            }
        }
    };

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

    public int level, exp;
    public boolean hasRigging;
    public enums.shipClass shipclass;

    protected EntityKansenBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return super.getCapability(capability, facing);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STORAGE, new CompoundNBT());
        this.dataManager.register(DATA_ID_RIGGING, this.hasRigging);
        this.dataManager.register(SITTING, this.isSitting());
        this.dataManager.register(ITEM_RIGGING,ItemStack.EMPTY);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("inventory",this.ShipStorage.serializeNBT());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.ShipStorage.deserializeNBT((CompoundNBT) compound.get("inventory"));
    }

    public void setShipClass(enums.shipClass setclass){
        this.shipclass = setclass;
    }

    public enums.shipClass getShipClass(){
        return this.shipclass;
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
        return this.getRigging() != ItemStack.EMPTY;
    }

    public boolean canSail(){
        float f = this.getEyeHeight() - 1F;
        return this.isInWater() && this.func_233571_b_(FluidTags.WATER) > (double)f && this.Hasrigging();
        //return this.isInWater() && this.func_233571_b_(FluidTags.WATER) > (double)f && this.Hasrigging();
    }

    @Override
    public void setTamedBy(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerId(player.getUniqueID());
    }

    public ItemStack getRigging(){
        return this.dataManager.get(ITEM_RIGGING);
    }


    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if(this.isOwner(player)){
            if(player.isSneaking()){
                if(!world.isRemote) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new ContainerKansenInventory.Supplier(this));
                    return ActionResultType.SUCCESS;
                }
            }
            else{
                this.func_233687_w_(!this.isSitting());
                return ActionResultType.SUCCESS;
            }
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        ItemRiggingBase riggingitem = (ItemRiggingBase) this.getRigging().getItem();
        if(this.rand.nextInt(100)<=75) {
            riggingitem.damageRigging((int) amount);
            return true;
        }
        else
            return super.attackEntityFrom(source, amount);
    }

    public boolean hasHelmet(){
        return this.ShipStorage.getStackInSlot(3).isEmpty();
    }

    public boolean haschestplate(){
        return this.ShipStorage.getStackInSlot(4).isEmpty();
    }

    public boolean hasleggings(){
        return this.ShipStorage.getStackInSlot(5).isEmpty();
    }

    public boolean hasboots(){
        return this.ShipStorage.getStackInSlot(6).isEmpty();
    }

    public boolean hasarmor(){
        return this.hasHelmet()||this.haschestplate()||this.hasleggings()||this.hasboots();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new KansenSwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
    }

    @Override
    public void tick() {
        if(!this.isInWater() && !this.isSitting() && !this.isSleeping()) {
            this.navigator.getNodeProcessor().setCanEnterDoors(true);
            this.navigator.getNodeProcessor().setCanOpenDoors(true);
        }
        if (canSail()) {
            this.kansenFloat();
        }

        super.tick();
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

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    protected  <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        return null;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
