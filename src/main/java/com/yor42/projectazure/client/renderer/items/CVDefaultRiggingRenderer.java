package com.yor42.projectazure.client.renderer.items;

import com.yor42.projectazure.client.model.rigging.modelCVRiggingDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class CVDefaultRiggingRenderer extends GeoItemRenderer<ItemRiggingCVDefault> {
    public CVDefaultRiggingRenderer() {
        super(new modelCVRiggingDefault());
    }
}
