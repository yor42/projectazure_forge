package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMixinPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Mixin(value = PlayerEntity.class, priority = 1500)
public abstract class MixinPlayerEntity extends LivingEntity implements IMixinPlayerEntity {
    private static final DataParameter<CompoundNBT> DATA_BACK = EntityDataManager.defineId(PlayerEntity.class, DataSerializers.COMPOUND_TAG);

    @Unique
    private long timeEntitySatOnBack;

    @Inject(method = "defineSynchedData", at=@At("TAIL"))
    private void onDefineSynchedData(CallbackInfo ci){
        this.entityData.define(DATA_BACK, new CompoundNBT());
    }

    @Inject(method = "addAdditionalSaveData", at=@At("TAIL"))
    private void onSave(CompoundNBT compound, CallbackInfo ci){
        compound.put("entityonback", this.getEntityData().get(DATA_BACK));
    }

    @Inject(method = "readAdditionalSaveData", at=@At("TAIL"))
    private void onLoad(CompoundNBT compound, CallbackInfo ci){
        this.getEntityData().set(DATA_BACK, compound.getCompound("entityonback"));
    }

    @Inject(method = "aiStep", at=@At("TAIL"))
    private void onAIStep(CallbackInfo ci){
        if (!this.level.isClientSide && this.isSleeping()) {
            this.removeEntityOnBack();
        }

        if(this.tickCount %1200 !=0 || this.getEntityonBack().isEmpty()){
            return;
        }

        CompoundNBT entityonBack = this.getEntityonBack();
        float affection = entityonBack.getFloat("affection");
        AtomicReference<Float> maxaffection = new AtomicReference<>((float) 100);
        double morale = entityonBack.getDouble("morale");
        double maxmorale = 150;
        EntityType.create(entityonBack, this.level).ifPresent((entity) -> {
            if (entity instanceof AbstractEntityCompanion) {
                maxaffection.set((float) ((AbstractEntityCompanion) entity).getmaxAffection());
            }
        });
        entityonBack.putFloat("affection", (float) Math.min(affection+0.5, maxaffection.get()));
        entityonBack.putDouble("morale", (float) Math.min(morale+5, maxmorale));
    }

    @Inject(method = "isSwimming", at=@At("HEAD"), cancellable = true)
    private void onisSwimming(CallbackInfoReturnable<Boolean> cir) {
        if(!this.getEntityonBack().isEmpty()){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Override
    public CompoundNBT getEntityonBack() {
        return this.getEntityData().get(DATA_BACK);
    }

    @Override
    public boolean setEntityonBack(CompoundNBT compound) {

        if (!this.onGround || this.isInWater()) {
            return false;
        }

        if (this.getEntityonBack().isEmpty()) {
            this.forcesetEntityonBack(compound);
            this.timeEntitySatOnBack = this.level.getGameTime();
            return true;
        }
        return false;
    }

    @Override
    public void forcesetEntityonBack(CompoundNBT compound) {
        this.entityData.set(DATA_BACK, new CompoundNBT());
    }

    @Override
    public Optional<Entity> removeEntityOnBack() {
        Optional<Entity> val = Optional.empty();
        if (this.timeEntitySatOnBack + 20L < this.level.getGameTime()) {
            val = this.respawnEntityOnBack(this.getEntityonBack());
            this.setEntityonBack(new CompoundNBT());
        }
        return val;
    }

    @Override
    public Optional<Entity> respawnEntityOnBack(CompoundNBT compound) {
        AtomicReference<Optional<Entity>> val = new AtomicReference<>(Optional.empty());
        if (!this.level.isClientSide && !compound.isEmpty()) {
            EntityType.create(compound, this.level).ifPresent((entity) -> {
                if (entity instanceof AbstractEntityCompanion) {
                    ((TameableEntity)entity).setOwnerUUID(this.uuid);
                }

                entity.setPos(this.getX(), this.getY() + (double)0.7F, this.getZ());
                ((ServerWorld)this.level).addWithUUID(entity);
                val.set(Optional.of(entity));

            });
        }
        return val.get();
    }

    @Override
    @Unique(silent = true)
    public void positionRider(@Nonnull Entity entity) {
        super.positionRider(entity);
    }

    //We do some hacky shit to prevent crash when other mod also overrides this. thanks Domi.
    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = {"positionRider", "func_184232_k"}, at = @At("HEAD"), remap = false, cancellable = true)
    private void onPositionRider(Entity entity, CallbackInfo cir){
        if(entity instanceof AbstractEntityCompanion){
            float entityhorizontalOffset = -0.3F;
            float f1 = (float)((this.removed ? (double)0.01F : this.getPassengersRidingOffset()) + entity.getMyRidingOffset());
            Vector3d vector3d = (new Vector3d(entityhorizontalOffset, 0.0D, 0.0D)).yRot(-this.yBodyRotO * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            entity.setPos(this.getX() + vector3d.x, this.getY() + f1, this.getZ() + vector3d.z);
            entity.yRot += this.yBodyRot;
            entity.setYHeadRot(entity.getYHeadRot() + this.yBodyRot);
            cir.cancel();
        }
    }

    @Override
    @Unique(silent = true)
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset();
    }

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Inject(method = {"getPassengersRidingOffset", "func_70042_X"}, at = @At("HEAD"), remap = false, cancellable = true)
    private void ongetPassengersRidingOffset(CallbackInfoReturnable<Double> cir){
        if(!this.getPassengers().isEmpty()){
            if(this.getPassengers().get(0) instanceof AbstractEntityCompanion){
                cir.setReturnValue(0D);
                cir.cancel();
            }
        }
    }

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }
}
