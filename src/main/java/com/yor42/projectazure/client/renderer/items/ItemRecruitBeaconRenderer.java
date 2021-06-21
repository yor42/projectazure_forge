package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.items.ModelItemRecruitBeacon;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemRecruitBeaconRenderer extends GeoItemRenderer {
    public ItemRecruitBeaconRenderer() {
        super(new ModelItemRecruitBeacon());
    }
}
