package com.yor42.projectazure.client.model.planes;

import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import com.yor42.projectazure.gameobject.items.ItemMissleDrone;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ModelMissileDroneEntity extends AnimatedGeoModel<EntityMissileDrone> {
    @Override
    public ResourceLocation getModelLocation(EntityMissileDrone object) {
        return ModResourceLocation("geo/misc/airplanes/modelmissiledrone.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMissileDrone object) {
        return ModResourceLocation("textures/planes/missiledrone.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMissileDrone animatable) {
        return ModResourceLocation("animations/entity/misc/missiledrone.animation.json");
    }
}
