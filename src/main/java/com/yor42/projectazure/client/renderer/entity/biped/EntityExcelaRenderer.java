package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.bonus.ModelExcela;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityExcela;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class EntityExcelaRenderer extends GeoCompanionRenderer<EntityExcela> {

    public EntityExcelaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelExcela());
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityExcela currentEntity) {

        if(currentEntity.isOrderedToSit() || currentEntity.isBeingPatted()){
            return null;
        }

        return super.getHeldItemForBone(boneName, currentEntity);
    }

}
