package com.yor42.projectazure.client.renderer.items;


import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DDDefaultRiggingRenderer extends GeoItemRenderer<itemRiggingDDDefault> {
    public DDDefaultRiggingRenderer() {
        super(new modelDDRiggingDefault());
    }

}
