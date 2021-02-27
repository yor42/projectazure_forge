package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.planes.modelPlaneF4FWildCat;
import com.yor42.projectazure.gameobject.items.equipment.ItemPlanef4fwildcat;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemPlanef4fWildcatRenderer extends GeoItemRenderer<ItemPlanef4fwildcat> {
    public ItemPlanef4fWildcatRenderer() {
        super(new modelPlaneF4FWildCat());
    }
}
