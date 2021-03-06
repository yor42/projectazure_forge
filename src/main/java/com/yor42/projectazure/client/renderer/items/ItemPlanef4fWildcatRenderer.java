package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.items.modelItemPlaneF4FWildCat;
import com.yor42.projectazure.gameobject.items.equipment.ItemPlanef4Fwildcat;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemPlanef4fWildcatRenderer extends GeoItemRenderer<ItemPlanef4Fwildcat> {
    public ItemPlanef4fWildcatRenderer() {
        super(new modelItemPlaneF4FWildCat());
    }
}
