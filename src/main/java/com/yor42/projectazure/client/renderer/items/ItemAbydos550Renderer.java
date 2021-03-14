package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.items.modelItemAbydos550;
import com.yor42.projectazure.gameobject.items.gun.ItemAbydos550;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemAbydos550Renderer extends GeoItemRenderer<ItemAbydos550> {
    public ItemAbydos550Renderer() {
        super(new modelItemAbydos550());
    }
}
