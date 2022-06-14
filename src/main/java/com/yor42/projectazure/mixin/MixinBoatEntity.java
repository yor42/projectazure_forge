package com.yor42.projectazure.mixin;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class MixinBoatEntity extends Entity {
    public MixinBoatEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "positionRider", at = @At("TAIL"))
    private void onPositionRider(Entity entity, CallbackInfo ci){
        if(entity instanceof AbstractEntityCompanion){
            if (this.getPassengers().size() > 1) {
                int j = entity.getId() % 2 == 0 ? -90 : -270;
                entity.setYBodyRot(((AnimalEntity)entity).yBodyRot + (float)j);
                entity.setYHeadRot(entity.getYHeadRot() + (float)j);
            }
        }
    }
}
