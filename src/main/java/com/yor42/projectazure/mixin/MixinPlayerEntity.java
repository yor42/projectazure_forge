package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMixinPlayerEntity;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Mixin(value = Player.class, priority = 1500)
public abstract class MixinPlayerEntity extends LivingEntity implements IMixinPlayerEntity {
    @Shadow public abstract void playSound(SoundEvent p_184185_1_, float p_184185_2_, float p_184185_3_);

    @Unique
    private static final EntityDataAccessor<CompoundTag> DATA_BACK = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);

    @Unique
    @Nullable
    private AbstractEntityCompanion companionOnBack;

    @Unique
    private long timeEntitySatOnBack;

    @Inject(method = "defineSynchedData", at=@At("TAIL"))
    private void onDefineSynchedData(CallbackInfo ci){
        this.entityData.define(DATA_BACK, new CompoundTag());
    }

    @Inject(method = "addAdditionalSaveData", at=@At("TAIL"))
    private void onSave(CompoundTag compound, CallbackInfo ci){
        compound.put("entityonback", this.getEntityData().get(DATA_BACK));
    }

    @Inject(method = "readAdditionalSaveData", at=@At("TAIL"))
    private void onLoad(CompoundTag compound, CallbackInfo ci){
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

        CompoundTag entityonBack = this.getEntityonBack();

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
    public CompoundTag getEntityonBack() {
        return this.getEntityData().get(DATA_BACK);
    }

    @Override
    public boolean setEntityonBack(CompoundTag compound) {

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
    public void forcesetEntityonBack(CompoundTag compound) {
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
        this.forcesetEntityonBack(new CompoundTag());
        return val;
    }

    @Override
    public Optional<Entity> respawnEntityOnBack(CompoundTag compound) {
        AtomicReference<Optional<Entity>> val = new AtomicReference<>(Optional.empty());
        if (!this.level.isClientSide && !compound.isEmpty()) {
            EntityType.create(compound, this.level).ifPresent((entity) -> {
                if (entity instanceof AbstractEntityCompanion) {
                    ((TamableAnimal) entity).setOwnerUUID(this.uuid);
                }

                entity.setPos(this.getX(), this.getY() + (double) 0.7F, this.getZ());
                ((ServerLevel) this.level).addWithUUID(entity);
                val.set(Optional.of(entity));

            });
        }
        return val.get();
    }

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> p_i48577_1_, Level p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }
}
