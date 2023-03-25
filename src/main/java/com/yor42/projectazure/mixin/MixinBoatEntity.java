package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class MixinBoatEntity extends Entity {
    public MixinBoatEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "positionRider", at = @At("TAIL"), cancellable = true)
    private void onPositionRider(Entity entity, CallbackInfo ci){
        if(entity instanceof AbstractEntityCompanion){
            if (this.getPassengers().size() > 1) {
                entity.setYBodyRot(((AbstractEntityCompanion)entity).yBodyRot);
                entity.setYHeadRot(entity.getYHeadRot());
                ci.cancel();
            }
        }
    }
}
