package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.magicuser.RosmontisModel;
import com.yor42.projectazure.client.renderer.layer.RosmontisClaymoreLayer;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

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

}
