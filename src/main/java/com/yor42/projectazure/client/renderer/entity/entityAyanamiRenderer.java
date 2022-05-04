package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.kansen.ayanamiModel;
import com.yor42.projectazure.client.renderer.layer.AyanamiRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityAyanami;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class entityAyanamiRenderer extends GeoCompanionRenderer<EntityAyanami> {
    public entityAyanamiRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ayanamiModel());
        this.addLayer(new AyanamiRiggingLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAyanami entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modelayanami.png");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1, 1.35F);
    }

}

