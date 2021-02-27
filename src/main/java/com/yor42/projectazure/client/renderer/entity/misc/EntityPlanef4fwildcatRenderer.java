package com.yor42.projectazure.client.renderer.entity.misc;

import com.yor42.projectazure.client.model.planes.modelPlaneF4FWildCat;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class EntityPlanef4fwildcatRenderer extends GeoEntityRenderer<EntityF4fWildcat> {
    protected EntityPlanef4fwildcatRenderer(EntityRendererManager renderManager) {
        super(renderManager, new modelPlaneF4FWildCat());
    }
}
