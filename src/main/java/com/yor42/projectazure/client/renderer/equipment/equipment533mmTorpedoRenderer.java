package com.yor42.projectazure.client.renderer.equipment;

import com.yor42.projectazure.client.model.equipments.modelEquipmentTorpedo533mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentTorpedo533Mm;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class equipment533mmTorpedoRenderer extends GeoItemRenderer<ItemEquipmentTorpedo533Mm> {

    public equipment533mmTorpedoRenderer() {
        super(new modelEquipmentTorpedo533mm());
    }
}
