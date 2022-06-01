package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.entity.bonus.ModelCrownslayer;
import com.yor42.projectazure.client.model.entity.sworduser.ModelYato;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityCrownSlayer;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityYato;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityYatoRenderer extends GeoCompanionRenderer<EntityYato> {
    public EntityYatoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelYato());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityYato entity) {
        return TextureEntityLocation("modelyato");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.3F, 1.5F);
    }
}
