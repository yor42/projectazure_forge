package com.yor42.projectazure.intermod.tconstruct.datagen;

import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;

import javax.annotation.Nonnull;

public class PAMaterialSpriteGenerator extends AbstractMaterialSpriteProvider {


    @Nonnull
    @Override
    public String getName() {
        return "Project Azure: Material Sprites";
    }

    @Override
    protected void addAllMaterials() {
        this.buildMaterial(PAMaterialProvider.D32).meleeHarvest().fallbacks("metal").colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, 0xFF274850).addARGB(102, 0xFF7099aa).addARGB(140, 0xFF8aafbe).addARGB(178, 0xFF96bac9).addARGB(216, 0xFF96bac9).addARGB(255, 0xFFc2dbec).build());
        this.buildMaterial(PAMaterialProvider.RMA70_12).meleeHarvest().fallbacks("metal").colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, 0xFF2c0000).addARGB(102, 0xFF4d3424).addARGB(140, 0xFF6f4f2d).addARGB(178, 0xFFaf8653).addARGB(216, 0xFFe6c580).addARGB(255, 0xFFfffed5).build());
    }
}
