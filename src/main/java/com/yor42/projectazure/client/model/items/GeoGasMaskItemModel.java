package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.GasMaskItem;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeoGasMaskItemModel extends AnimatedGeoModel<GasMaskItem> {
    @Override
    public ResourceLocation getModelLocation(GasMaskItem object) {
        return ResourceUtils.ModelLocation("item/gasmask.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GasMaskItem object) {
        return ResourceUtils.TextureLocation("armor/gasmask");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GasMaskItem animatable) {
        return ResourceUtils.AnimationLocation("block/machine/metal_press.animation.json");
    }
}
