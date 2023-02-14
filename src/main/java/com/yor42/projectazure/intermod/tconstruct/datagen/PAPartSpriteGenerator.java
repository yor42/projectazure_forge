package com.yor42.projectazure.intermod.tconstruct.datagen;

import com.yor42.projectazure.libs.Constants;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

public class PAPartSpriteGenerator extends AbstractPartSpriteProvider {
    public PAPartSpriteGenerator() {
        super(Constants.MODID);
    }

    public String getName() {
        return "Project Azure Parts";
    }

    @Override
    protected void addAllSpites() {

    }
}
