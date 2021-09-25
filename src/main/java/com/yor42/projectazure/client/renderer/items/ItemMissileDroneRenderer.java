package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.items.ModelItemMissileDrone;
import com.yor42.projectazure.gameobject.items.ItemMissleDrone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemMissileDroneRenderer extends GeoItemRenderer<ItemMissleDrone> {
    public ItemMissileDroneRenderer() {
        super(new ModelItemMissileDrone());
    }
}
