package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.items.ModelDefibPaddle;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemDefibPaddleRenderer extends GeoItemRenderer<ItemDefibPaddle> {
    public ItemDefibPaddleRenderer() {
        super(new ModelDefibPaddle());
    }
}
