package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.items.ModelItemClaymore;
import com.yor42.projectazure.gameobject.items.tools.ItemClaymore;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemClaymoreRenderer extends GeoItemRenderer<ItemClaymore> {
    public ItemClaymoreRenderer() {
        super(new ModelItemClaymore());
    }
}
