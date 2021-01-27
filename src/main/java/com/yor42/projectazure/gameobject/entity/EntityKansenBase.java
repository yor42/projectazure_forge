package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.ai.KansenSwimGoal;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public abstract class EntityKansenBase extends TameableEntity implements IAnimatable {

    private static final int SLOT_OFFHAND = 1;
    private static final int SLOT_MAINHAND = 2;


    private static final DataParameter<Boolean> DATA_ID_RIGGING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);
    public static final DataParameter<CompoundNBT> STORAGE = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.BOOLEAN);

    public static final DataParameter<ItemStack> ITEM_MAINHAND = EntityDataManager.createKey(EntityKansenBase.class, DataSerializers.ITEMSTACK);


    public ItemStackHandler ShipStorage = new ItemStackHandler(19) {
        @Override
        protected void onContentsChanged(int slot) {
            switch (slot)
            {
                case SLOT_MAINHAND:
                    EntityKansenBase.this.dataManager.set(ITEM_MAINHAND, this.getStackInSlot(slot));
                    break;
            }
        }
    };

    public int level, exp;
    public boolean hasRigging;
    public shipClass shipclass;

    protected EntityKansenBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public ItemStack getHeldItemMainhand() {
        return this.dataManager.get(ITEM_MAINHAND);
    }

    @Override
    public ItemStack getHeldItemOffhand() {
        return this.ShipStorage.getStackInSlot(1);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STORAGE, new CompoundNBT());
        this.dataManager.register(DATA_ID_RIGGING, this.hasRigging);
        this.dataManager.register(SITTING, this.isSitting());
        this.dataManager.register(ITEM_MAINHAND,ItemStack.EMPTY);
    }

    public void setRigging(boolean hasrigging){
        this.hasRigging = hasrigging;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("HasRigging", this.Hasrigging());
        compound.put("inventory",this.ShipStorage.serializeNBT());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setRigging(compound.getBoolean("HasRigging"));
        this.ShipStorage.deserializeNBT((CompoundNBT) compound.get("inventory"));
    }

    public void setShipClass(shipClass setclass){
        this.shipclass = setclass;
    }

    public shipClass getShipClass(){
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
        return this.hasRigging;
    }

    public boolean isSailing(){
        float f = this.getEyeHeight() - 1F;
        return this.isInWater() && this.func_233571_b_(FluidTags.WATER) > (double)f;
        //return this.isInWater() && this.func_233571_b_(FluidTags.WATER) > (double)f && this.Hasrigging();
    }

    @Override
    public void setTamedBy(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerId(player.getUniqueID());
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
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        switch(slotIn)
        {
            case HEAD:
                return this.ShipStorage.getStackInSlot(3);

            case CHEST:
                return this.ShipStorage.getStackInSlot(4);

            case LEGS:
                return this.ShipStorage.getStackInSlot(5);

            case FEET:
                return this.ShipStorage.getStackInSlot(6);

            case MAINHAND:
                return this.ShipStorage.getStackInSlot(2);

            case OFFHAND:
                return this.ShipStorage.getStackInSlot(1);

            default:
                return ItemStack.EMPTY;
        }
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
        if (isSailing()) {
            this.kansenFloat();
        }

        this.updateStorage();

        super.tick();
    }

    protected void updateStorage(){
        this.dataManager.set(STORAGE, this.ShipStorage.serializeNBT());
        this.dataManager.set(ITEM_MAINHAND, this.ShipStorage.getStackInSlot(2));
    };

    public ItemStack getStackinMainHand(){
        return this.dataManager.get(ITEM_MAINHAND);
    }

    public ItemStackHandler getShipStorage() {
        return ShipStorage;
    }

    private void kansenFloat() {
        Vector3d vec3d = this.getMotion();
        this.setVelocity(vec3d.x * 0.9900000095367432D, vec3d.y + (double)(vec3d.y < 0.05999999865889549D ? 5.0E-4F : 0.0F), vec3d.z * 0.9900000095367432D);
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }


    public enum shipClass {
        Destroyer,
        LightCruiser,
        HeavyCruiser,
        LargeCruiser,
        Battleship,
        AircraftCarrier,
        LightAircraftCarrier,
        Submarine,
        SubmarineCarrier,
        MonitorShip,
        Repair
    }

}
