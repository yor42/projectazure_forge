package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.magicuser.RosmontisModel;
import com.yor42.projectazure.client.renderer.layer.RosmontisClaymoreLayer;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityRosmontisRenderer extends GeoCompanionRenderer<EntityRosmontis> {

    public EntityRosmontisRenderer(EntityRendererManager renderManager) {
        super(renderManager, new RosmontisModel());
        this.addLayer(new RosmontisClaymoreLayer(this));
    }
    @Override
    public ResourceLocation getTextureLocation(EntityRosmontis entity) {
        return TextureEntityLocation("modelrosmontis");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.5F, 0.1F, 1.3F);
    }

}
