package com.yor42.projectazure.client.renderer.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.items.ModelDefibCharger;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibCharger;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.energy.CapabilityEnergy;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ItemDefibChargerRenderer extends GeoItemRenderer<ItemDefibCharger> {
    public ItemDefibChargerRenderer() {
        super(new ModelDefibCharger());
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        boolean shouldRender = this.shouldRender(bone);
        if(shouldRender) {
            if (bone.getName().contains("emissive")) {
                packedLightIn = LightTexture.pack(15, 15);
            }
            super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    private boolean shouldRender(GeoBone bone) {
        String name = bone.getName();
        float energy = this.currentItemStack.getCapability(CapabilityEnergy.ENERGY).map((e) -> (float)e.getEnergyStored() / (float)e.getMaxEnergyStored()).orElse(0F);
        float charge = (float)(ItemDefibCharger.getChargeProgress(this.currentItemStack))/100f;
        switch (name) {
            default:
                return true;
            case "emissive_battery1":
                return energy >= 0.2f;
            case "emissive_battery2":
                return energy >= 0.4f;
            case "emissive_battery3":
                return energy >= 0.6f;
            case "emissive_battery4":
                return energy >= 0.8f;
            case "emissive_charge1":
                return charge >= 0.2f;
            case "emissive_charge2":
                return charge >= 0.4f;
            case "emissive_charge3":
                return charge >= 0.6f;
            case "emissive_charge4":
                return charge >= 0.8f;
            case "emissive_charge5":
                return charge == 1f;
            case "emissive_on":
                return ItemDefibCharger.isOn(this.currentItemStack) && ItemDefibCharger.getChargeProgress(this.currentItemStack) == 0;
            case "emissive_ready":
                return ItemDefibCharger.isOn(this.currentItemStack) && ItemDefibCharger.getChargeProgress(this.currentItemStack) == 100;
            case "emissive_charging":
                return ItemDefibCharger.isOn(this.currentItemStack) && ItemDefibCharger.getChargeProgress(this.currentItemStack) <100 && ItemDefibCharger.isOn(this.currentItemStack) && ItemDefibCharger.getChargeProgress(this.currentItemStack) >0;

        }
    }
}
