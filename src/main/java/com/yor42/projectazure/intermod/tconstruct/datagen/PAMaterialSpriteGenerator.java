package com.yor42.projectazure.intermod.tconstruct.datagen;

import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;

import javax.annotation.Nonnull;

public class PAMaterialSpriteGenerator extends AbstractMaterialSpriteProvider {


    @Nonnull
    @Override
    public String getName() {
        return "projectazure_materials";
    }

    @Override
    protected void addAllMaterials() {
        this.buildMaterial(PAMaterialProvider.D32).meleeHarvest().fallbacks("metal").colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, 0xFFc2dbec).addARGB(102, 0xFF96bac9).addARGB(140, 0xFF8aafbe).addARGB(178, 0xFF7099aa).addARGB(216, 0xFF37718a).addARGB(255, 0xFF274850).build());
    }
}
