package com.yor42.projectazure.client.renderer.equipment;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.modelEquipmentTorpedo533mm;
import com.yor42.projectazure.gameobject.items.ItemEquipmentTorpedo533mm;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class equipment533mmTorpedoRenderer extends GeoItemRenderer<ItemEquipmentTorpedo533mm> {

    public equipment533mmTorpedoRenderer() {
        super(new modelEquipmentTorpedo533mm());
    }
}
