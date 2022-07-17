package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.GeoCompanionModel;
import com.yor42.projectazure.client.model.entity.bonus.ModelExcela;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityExcela;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EntityExcelaRenderer extends GeoCompanionRenderer<EntityExcela> {

    public EntityExcelaRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelExcela());
    }
}
