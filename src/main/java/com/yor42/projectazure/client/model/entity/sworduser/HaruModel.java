package com.yor42.projectazure.client.model.entity.sworduser;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityHaru;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.util.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.GeoModelEntityLocation;
import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class HaruModel extends GeoCompanionModel<EntityHaru> {

    @Override
    public ResourceLocation getModelLocation(EntityHaru object) {
        return GeoModelEntityLocation("modelharu");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHaru object) {
        return TextureEntityLocation("entityharu");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHaru animatable) {

        return ResourceUtils.ModResourceLocation("animations/entity/sworduser/haru.animation.json");
    }
}
