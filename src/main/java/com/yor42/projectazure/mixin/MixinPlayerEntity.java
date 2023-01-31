package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@Mixin(value = PlayerEntity.class, priority = 1500)
public abstract class MixinPlayerEntity extends LivingEntity {

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
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
            float entityhorizontalOffset = -0.65F;
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
}
