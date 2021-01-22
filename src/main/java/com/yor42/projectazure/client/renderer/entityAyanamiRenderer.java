package com.yor42.projectazure.client.renderer;

import com.yor42.projectazure.client.model.ayanamiModel;
import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class entityAyanamiRenderer extends GeoEntityRenderer<EntityAyanami> {
    protected entityAyanamiRenderer(EntityRendererManager renderManager, AnimatedGeoModel<EntityAyanami> modelProvider) {
        super(renderManager, new ayanamiModel());
        this.shadowSize = 0.7F; //change 0.7 to the desired shadow size.
    }
}
