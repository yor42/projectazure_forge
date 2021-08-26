package com.yor42.projectazure.client.renderer.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.items.ModelItemMissileDrone;
import com.yor42.projectazure.gameobject.items.ItemMissleDrone;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemMissileDroneRenderer extends GeoItemRenderer<ItemMissleDrone> {
    public ItemMissileDroneRenderer() {
        super(new ModelItemMissileDrone());
    }
}
