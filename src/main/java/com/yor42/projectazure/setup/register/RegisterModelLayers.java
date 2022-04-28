package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class RegisterModelLayers {
    public static final ModelLayerLocation MISSILE = register("missile");
    public static final ModelLayerLocation FIREARM_BULLET = register("bullet");

    private static ModelLayerLocation register(String p_171296_) {
        ModelLayerLocation modellayerlocation = createLocation(p_171296_, "main");
        return modellayerlocation;
    }
    private static ModelLayerLocation createLocation(String p_171301_, String p_171302_) {
        return new ModelLayerLocation(new ResourceLocation(Constants.MODID, p_171301_), p_171302_);
    }

    public static void register(){}
}
