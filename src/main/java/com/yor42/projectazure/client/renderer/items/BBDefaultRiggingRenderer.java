package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.rigging.modelBBRiggingDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBBDefault;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class BBDefaultRiggingRenderer extends GeoItemRenderer<ItemRiggingBBDefault> {
    public BBDefaultRiggingRenderer() {
        super(new modelBBRiggingDefault());
    }
}
