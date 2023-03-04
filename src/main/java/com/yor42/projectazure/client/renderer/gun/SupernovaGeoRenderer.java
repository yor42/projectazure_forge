package com.yor42.projectazure.client.renderer.gun;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.items.ModelSupernova_geo;
import com.yor42.projectazure.client.model.items.ModelTyphoon_geo;
import com.yor42.projectazure.gameobject.items.GeoGunItem;
import com.yor42.projectazure.gameobject.items.ItemEnergyGun;
import net.minecraftforge.energy.CapabilityEnergy;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SupernovaGeoRenderer extends GeoItemRenderer<GeoGunItem> {
    public SupernovaGeoRenderer() {
        super(new ModelSupernova_geo());
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if(bone.getName().contains("emissive")){
            GeoGunItem energygun = (GeoGunItem) this.currentItemStack.getItem();

            if(this.currentItemStack.getCapability(CapabilityEnergy.ENERGY).map((energyhandler)-> energyhandler.extractEnergy(energygun.getEnergyperShot(), true)  == energygun.getEnergyperShot()).orElse(false)){
                packedLightIn = 15728880;
            }
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

}
