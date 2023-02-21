package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityLogisticsDrone extends CreatureEntity implements IAnimatable {

    protected final CustomEnergyStorage battery;
    protected final ItemStackHandler inventory;
    protected static final DataParameter<Optional<BlockPos>> HOMEPOSITION = EntityDataManager.defineId(EntityLogisticsDrone.class, DataSerializers.OPTIONAL_BLOCK_POS);


    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    protected EntityLogisticsDrone(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
        this.battery = new CustomEnergyStorage(2500, 100, 100);
        this.inventory = new ItemStackHandler(9);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "propeller_controller", 2, this::propeller_predicate));
        data.addAnimationController(new AnimationController<>(this, "body_controller", 0, this::body_predicate));
    }

    private <T extends IAnimatable> PlayState body_predicate(AnimationEvent<T> tAnimationEvent) {
        return PlayState.STOP;
    }

    private <T extends IAnimatable> PlayState propeller_predicate(AnimationEvent<T> tAnimationEvent) {
        return PlayState.STOP;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOMEPOSITION, Optional.empty());
    }

    @Nullable
    public BlockPos getHomePosition(){
        return this.getEntityData().get(HOMEPOSITION).orElse(null);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        this.getEntityData().get(HOMEPOSITION).ifPresent((blockpos)->{
            compound.put("homeposition", MathUtil.serializeBlockPos(blockpos));
        });
        compound.put("inventory", this.inventory.serializeNBT());
        compound.put("battery", this.battery.serializeNBT());

    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("homeposition")){
            this.getEntityData().set(HOMEPOSITION, Optional.of(MathUtil.deserializeBlockPos(compound.getCompound("homeposition"))));
        }

        this.inventory.deserializeNBT(compound.getCompound("inventory"));
        this.battery.deserializeNBT(compound.getCompound("battery"));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
