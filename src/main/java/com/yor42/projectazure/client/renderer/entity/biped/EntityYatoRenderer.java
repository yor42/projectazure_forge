package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.ModelYato;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityYato;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityYatoRenderer extends GeoCompanionRenderer<EntityYato> {
    public EntityYatoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelYato());
    }

    @Override
    protected boolean shouldHideBone(String bone) {

        if(bone.contains("visor")){
            return !(this.currentEntityBeingRendered.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR);
        }

        return super.shouldHideBone(bone);
    }

    @Override
    protected boolean isBoneCosmetic(GeoBone bone) {
        if(bone.getName().contains("visor")){
            return true;
        }
        return super.isBoneCosmetic(bone);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityYato entity) {
        return TextureEntityLocation("modelyato");
    }

}
