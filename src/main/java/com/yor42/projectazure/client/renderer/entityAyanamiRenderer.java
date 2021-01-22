package com.yor42.projectazure.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.ayanamiModel;
import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class entityAyanamiRenderer extends GeoEntityRenderer<EntityAyanami> {
    public entityAyanamiRenderer(EntityRendererManager renderManager/*, AnimatedGeoModel<EntityAyanami> modelProvider*/) {
        super(renderManager, new ayanamiModel());
        this.shadowSize = 0.3F; //change 0.7 to the desired shadow size.

    }

}

