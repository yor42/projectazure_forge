package com.yor42.projectazure.intermod.tconstruct.datagen;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

import javax.annotation.Nullable;

public class PAMaterialRenderInfoProvider extends AbstractMaterialRenderInfoProvider {
    public PAMaterialRenderInfoProvider(DataGenerator gen, @Nullable AbstractMaterialSpriteProvider materialSprites) {
        super(gen, materialSprites);
    }

    @Override
    protected void addMaterialRenderInfo() {
        buildRenderInfo(PAMaterialProvider.D32);
    }

    @Override
    public String getName() {
        return "Project Azure TConstruct Material RenderInfo";
    }
}
