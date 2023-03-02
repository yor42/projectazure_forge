package com.yor42.projectazure.client.renderer.items;


import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;

public class DDDefaultRiggingRenderer extends AbstractRiggingRenderer<itemRiggingDDDefault> {
    public DDDefaultRiggingRenderer() {
        super(new modelDDRiggingDefault());
    }

}
