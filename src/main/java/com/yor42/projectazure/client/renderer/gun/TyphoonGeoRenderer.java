package com.yor42.projectazure.client.renderer.gun;

import com.yor42.projectazure.client.model.items.ModelTyphoon_geo;
import com.yor42.projectazure.gameobject.items.GeoGunItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class TyphoonGeoRenderer extends GeoItemRenderer<GeoGunItem> {
    public TyphoonGeoRenderer() {
        super(new ModelTyphoon_geo());
    }
}
