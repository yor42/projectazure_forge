package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public abstract class MixinExperienceOrbEntity extends Entity {
    @Shadow
    private PlayerEntity followingPlayer;
    private AbstractEntityCompanion followingCompanion;
    @Shadow
    private int followingTime;
    @Shadow
    public int tickCount;

    public MixinExperienceOrbEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci){

        if (this.followingTime < this.tickCount - 20 + this.getId() % 100) {
            if (this.followingPlayer == null || this.followingPlayer.distanceToSqr(this) > 64.0D) {
                this.followingCompanion = this.level.getNearestEntity(AbstractEntityCompanion.class, EntityPredicate.DEFAULT, null, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(8));
            }

            this.followingTime = this.tickCount;
        }

        if(this.followingCompanion==null){
            return;
        }

        if(this.followingPlayer == null || this.distanceTo(this.followingCompanion) < this.distanceTo(this.followingPlayer)){
            Vector3d vector3d = new Vector3d(this.followingCompanion.getX() - this.getX(), this.followingCompanion.getY() + (double)this.followingCompanion.getEyeHeight() / 2.0D - this.getY(), this.followingCompanion.getZ() - this.getZ());
            double d1 = vector3d.lengthSqr();
            if (d1 < 64.0D) {
                double d2 = 1.0D - Math.sqrt(d1) / 8.0D;
                this.setDeltaMovement(this.getDeltaMovement().add(vector3d.normalize().scale(d2 * d2 * 0.1D)));
            }
        }

    }
}
