package com.yor42.projectazure.client.renderer.equipment;

import com.yor42.projectazure.client.model.ModelMaingun127mm;
import com.yor42.projectazure.gameobject.items.ItemEquipmentGun127mm;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class Equipment127mmGunRenderer extends GeoItemRenderer<ItemEquipmentGun127mm> {

    public Equipment127mmGunRenderer() {
        super(new ModelMaingun127mm());
    }
}
