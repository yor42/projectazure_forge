package com.yor42.projectazure.mixin;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMixinPlayerEntity;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
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
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Mixin(value = PlayerEntity.class, priority = 1500)
public abstract class MixinPlayerEntity extends LivingEntity implements IMixinPlayerEntity {
    @Shadow public abstract void playSound(SoundEvent p_184185_1_, float p_184185_2_, float p_184185_3_);

    @Unique
    private static final DataParameter<CompoundNBT> DATA_BACK = EntityDataManager.defineId(PlayerEntity.class, DataSerializers.COMPOUND_TAG);

    @Unique
    @Nullable
    private AbstractEntityCompanion companionOnBack;

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

        if(this.tickCount%400==0 && MathUtil.rollBooleanRNG() && this.companionOnBack!=null){
            this.playSound(this.companionOnBack.Ambientsoundgetter(),0.9F, 0.9F+(0.2F*MathUtil.rand.nextFloat()));
        }

        if(this.tickCount %1200 !=0 || this.getEntityonBack().isEmpty()){
            return;
        }

        CompoundNBT entityonBack = this.getEntityonBack();

        if(entityonBack.isEmpty()){
            return;
        }else if(this.companionOnBack == null){
            EntityType.create(entityonBack, this.level).ifPresent((entity)->{
                if(entity instanceof AbstractEntityCompanion){
                    this.companionOnBack = (AbstractEntityCompanion) entity;
                }
            });
        }

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

    @Inject(method = "die", at=@At("TAIL"))
    private void onDeath(DamageSource p_70645_1_, CallbackInfo ci){
        this.forceremoveEntityOnBack();
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
        if(!compound.isEmpty()) {
            Optional<Entity> ety = EntityType.create(compound, this.level);
            if (ety.isPresent() && ety.get() instanceof AbstractEntityCompanion) {
                this.companionOnBack = (AbstractEntityCompanion) ety.get();
            } else {
                this.companionOnBack = null;
            }
        }
        this.entityData.set(DATA_BACK, compound);
    }

    @Override
    public Optional<Entity> removeEntityOnBack() {
        if (this.timeEntitySatOnBack + 20L < this.level.getGameTime()) {
            return forceremoveEntityOnBack();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Entity> forceremoveEntityOnBack() {
        Optional<Entity> val;
        val = this.respawnEntityOnBack(this.getEntityonBack());
        this.forcesetEntityonBack(new CompoundNBT());
        return val;
    }

    @Override
    public Optional<Entity> respawnEntityOnBack(CompoundNBT compound) {
        AtomicReference<Optional<Entity>> val = new AtomicReference<>(Optional.empty());
        if (!this.level.isClientSide && !compound.isEmpty()) {
            EntityType.create(compound, this.level).ifPresent((entity) -> {
                if (entity instanceof AbstractEntityCompanion) {
                    ((TameableEntity) entity).setOwnerUUID(this.uuid);
                }

                entity.setPos(this.getX(), this.getY() + (double) 0.7F, this.getZ());
                ((ServerWorld) this.level).addWithUUID(entity);
                val.set(Optional.of(entity));

            });
        }
        return val.get();
    }

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }
}
