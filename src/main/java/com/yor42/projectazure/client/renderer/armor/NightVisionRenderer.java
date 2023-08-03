package com.yor42.projectazure.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.model.armor.GeoNightvisionModel;
import com.yor42.projectazure.gameobject.items.ItemNightVisionHelmet;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class NightVisionRenderer extends GeoArmorRenderer<ItemNightVisionHelmet> {
    public NightVisionRenderer() {
        super(new GeoNightvisionModel());
        this.bodyBone = null;
        this.leftArmBone = null;
        this.rightArmBone = null;
        this.leftLegBone = null;
        this.rightLegBone = null;
        this.leftBootBone = null;
        this.rightBootBone = null;
    }
}
