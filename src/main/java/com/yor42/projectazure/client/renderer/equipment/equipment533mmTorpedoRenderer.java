package com.yor42.projectazure.client.renderer.equipment;

import com.yor42.projectazure.client.model.modelEquipmentTorpedo533mm;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentTorpedo533mm;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class equipment533mmTorpedoRenderer extends GeoItemRenderer<ItemEquipmentTorpedo533mm> {

    public equipment533mmTorpedoRenderer() {
        super(new modelEquipmentTorpedo533mm());
    }
}
