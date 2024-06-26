package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class EntityLogisticsDrone extends PathfinderMob implements IAnimatable {

    protected final CustomEnergyStorage battery;
    protected final ItemStackHandler inventory;
    protected static final EntityDataAccessor<Integer> CURRENTDESTINATIONINDEX = SynchedEntityData.defineId(EntityLogisticsDrone.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> NEXTDESTINATIONINDEX = SynchedEntityData.defineId(EntityLogisticsDrone.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> ISLOADING = SynchedEntityData.defineId(EntityLogisticsDrone.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(EntityLogisticsDrone.class, EntityDataSerializers.OPTIONAL_UUID);

    protected final ArrayList<BlockPos> DestinationList = new ArrayList<>();


    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    protected EntityLogisticsDrone(EntityType<? extends PathfinderMob> p_i48575_1_, Level p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
        this.battery = new CustomEnergyStorage(2500, 100, 100);
        this.inventory = new ItemStackHandler(9);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "propeller_controller", 0, this::propeller_predicate));
        data.addAnimationController(new AnimationController<>(this, "body_controller", 20, this::body_predicate));
    }

    private <T extends IAnimatable> PlayState body_predicate(AnimationEvent<T> tAnimationEvent) {
        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(!this.isOnGround()){
            tAnimationEvent.getController().setAnimation(builder.addAnimation("carriage_swing", ILoopType.EDefaultLoopTypes.LOOP));
        }


        return PlayState.STOP;
    }

    private <T extends IAnimatable> PlayState propeller_predicate(AnimationEvent<T> tAnimationEvent) {

        if(Minecraft.getInstance().isPaused()){
            return PlayState.STOP;
        }
        AnimationBuilder builder = new AnimationBuilder();

        if(this.getNavigation().getPath()!=null || !this.isOnGround()){
            tAnimationEvent.getController().setAnimation(builder.addAnimation("propeller_spin", ILoopType.EDefaultLoopTypes.LOOP));
        }


        return PlayState.STOP;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CURRENTDESTINATIONINDEX, -1);
        this.entityData.define(NEXTDESTINATIONINDEX, 0);
        this.entityData.define(ISLOADING, false);
    }


    public BlockPos getHomePosition(){
        return this.DestinationList.get(0);
    }

    public void addDestination(BlockPos pos){
        this.DestinationList.add(pos.above());
    }

    public BlockPos getNextDestination(){
        return this.DestinationList.get(this.entityData.get(NEXTDESTINATIONINDEX));
    }

    public CustomEnergyStorage getBattery() {
        return battery;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("currentdestinationindex", this.getEntityData().get(CURRENTDESTINATIONINDEX));
        compound.putInt("nextdestinationindex", this.getEntityData().get(NEXTDESTINATIONINDEX));
        compound.putBoolean("isloading", this.getEntityData().get(ISLOADING));
        this.getEntityData().get(OWNER).ifPresent((uuid)->{
            compound.putUUID("owner", uuid);
        });
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("battery", this.battery.serializeNBT());

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.getEntityData().set(CURRENTDESTINATIONINDEX, compound.getInt("currentdestinationindex"));
        this.getEntityData().set(NEXTDESTINATIONINDEX, compound.getInt("nextdestinationindex"));
        this.getEntityData().set(ISLOADING, compound.getBoolean("isloading"));

        if(compound.hasUUID("owner")){
            this.getEntityData().set(OWNER, Optional.of(compound.getUUID("owner")));
        }

        this.inventory.deserializeNBT(compound.getCompound("inventory"));
        this.battery.deserializeNBT(compound.getCompound("battery"));
    }

    @Override
    public InteractionResult interactAt(Player p_184199_1_, Vec3 p_184199_2_, InteractionHand p_184199_3_) {

        if(this.getEntityData().get(OWNER).isPresent() && this.getEntityData().get(OWNER).get() != p_184199_1_.getUUID()){
            return InteractionResult.FAIL;
        }

        if(this.entityData.get(CURRENTDESTINATIONINDEX) == -1 && !this.getEntityData().get(ISLOADING)){
            this.StartMovetoNextDestination();
            return InteractionResult.SUCCESS;
        }

        return super.interactAt(p_184199_1_, p_184199_2_, p_184199_3_);

    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(!this.onGround && this.battery.extractEnergy(2, false) !=2){
            this.getNavigation().stop();
            return;
        }

        if(this.getEntityData().get(CURRENTDESTINATIONINDEX)>=0){
            if(this.getNavigation().isDone()) {
                int Pathindex = this.getEntityData().get(CURRENTDESTINATIONINDEX);
                BlockPos destination = this.DestinationList.get(Pathindex);

                if (this.getNavigation().getTargetPos().equals(destination)) {
                    this.getNavigation().stop();
                    this.getEntityData().set(CURRENTDESTINATIONINDEX, -1);
                } else {
                    this.getNavigation().moveTo(destination.getX() + 0.5, destination.getY() + 0.5, destination.getZ() + 0.5, 1.0);
                }
            }
        }

    }

    public boolean StartMovetoNextDestination(){

        if(this.DestinationList.size()<2){
            return false;
        }
        if(this.getEntityData().get(CURRENTDESTINATIONINDEX) >=0){
            return false;
        }
        int nextindex = this.getEntityData().get(NEXTDESTINATIONINDEX)+1;
        if(nextindex == this.DestinationList.size()){
            nextindex =0;
        }
        this.getEntityData().set(CURRENTDESTINATIONINDEX, nextindex);
        return true;
    }

    public static AttributeSupplier.Builder MutableAttribute()
    {
        return Mob.createMobAttributes()
                //Attribute
                .add(Attributes.MOVEMENT_SPEED, 0.5F)
                .add(ForgeMod.SWIM_SPEED.get(), 0.0F)
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.FLYING_SPEED, 0.5F)
                .add(Attributes.ATTACK_DAMAGE, 2F)
                .add(Attributes.FOLLOW_RANGE, 128F)
                ;
    }

    @Override
    protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
        return 0;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
