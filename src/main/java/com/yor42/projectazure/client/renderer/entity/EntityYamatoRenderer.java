package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.kansen.YamatoModel;
import com.yor42.projectazure.client.renderer.layer.YamatoRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityEnterprise;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityYamato;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
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
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityYamatoRenderer extends GeoCompanionRenderer<EntityYamato> {

    public EntityYamatoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new YamatoModel());
        this.addLayer(new YamatoRiggingLayer(this));
        this.shadowRadius = 0.4F;
    }
    @Override
    public ResourceLocation getTextureLocation(EntityYamato instance) {
        return TextureEntityLocation("modelyamato");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.7F, 0.1F, 1.7F);
    }

}
